package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.LibraryRepository
import com.example.model.ActivityLog
import com.example.model.Book
import com.example.model.LibrarianUser
import com.example.model.Loan
import com.example.model.LoanStatus
import com.example.model.Student
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class LibraryStats(
    val totalBookTitles: Int = 0,
    val totalCopies: Int = 0,
    val totalAvailableCopies: Int = 0,
    val totalBorrowedCount: Int = 0,
    val totalOverdueCount: Int = 0,
    val totalOverdueFines: Double = 0.0,
    val totalStudentsCount: Int = 0
)

class LibraryViewModel(
    val repository: LibraryRepository = LibraryRepository()
) : ViewModel() {

    // Authentication state
    val isLoggedIn = MutableStateFlow(true) // Defaults to true for instant tablet admin access
    val librarian = MutableStateFlow(
        LibrarianUser(
            id = "lib-01",
            name = "Sarah Vance",
            email = "sarah.vance@libratrack.edu",
            role = "Head Librarian",
            badgeNumber = "LIB-7740"
        )
    )

    // Navigation
    private val _currentScreen = MutableStateFlow(Screen.DASHBOARD)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Screen selection history for BackHandler
    private val navigationStack = mutableListOf(Screen.DASHBOARD)

    // Selected book for details or editing
    val selectedBookId = MutableStateFlow<String?>(null)
    val editingBook = MutableStateFlow<Book?>(null)

    // Catalog filtering
    val catalogSearchQuery = MutableStateFlow("")
    val selectedGenre = MutableStateFlow("All")
    val selectedAvailability = MutableStateFlow("All")

    // Student filtering
    val studentSearchQuery = MutableStateFlow("")
    val selectedDepartment = MutableStateFlow("All")

    // Borrow/Return tab (0 = Issue/Borrow, 1 = Return Center)
    val borrowReturnTab = MutableStateFlow(0)
    val selectedBorrowStudentId = MutableStateFlow<String?>(null)
    val selectedBorrowBookId = MutableStateFlow<String?>(null)
    val loanDurationDays = MutableStateFlow(14)
    val borrowNotes = MutableStateFlow("")

    // Dialog & Feedback states
    val snackbarMessage = MutableStateFlow<String?>(null)
    val showAddStudentDialog = MutableStateFlow(false)
    val showExportDialog = MutableStateFlow(false)

    // Repository flows
    val books: StateFlow<List<Book>> = repository.books
    val students: StateFlow<List<Student>> = repository.students
    val loans: StateFlow<List<Loan>> = repository.loans
    val activities: StateFlow<List<ActivityLog>> = repository.activities

    // Computed Stats
    val stats: StateFlow<LibraryStats> = combine(books, students, loans) { bList, sList, lList ->
        val activeLoans = lList.filter { it.status == LoanStatus.ACTIVE }
        val overdueLoans = lList.filter { it.status == LoanStatus.OVERDUE }
        LibraryStats(
            totalBookTitles = bList.size,
            totalCopies = bList.sumOf { it.totalCopies },
            totalAvailableCopies = bList.sumOf { it.availableCopies },
            totalBorrowedCount = activeLoans.size + overdueLoans.size,
            totalOverdueCount = overdueLoans.size,
            totalOverdueFines = overdueLoans.sumOf { it.fineAmount },
            totalStudentsCount = sList.size
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LibraryStats())

    // Filtered books
    val filteredBooks: StateFlow<List<Book>> = combine(
        books,
        catalogSearchQuery,
        selectedGenre,
        selectedAvailability
    ) { bookList, query, genre, avail ->
        bookList.filter { book ->
            val matchesQuery = query.isBlank() ||
                    book.title.contains(query, ignoreCase = true) ||
                    book.author.contains(query, ignoreCase = true) ||
                    book.isbn.contains(query, ignoreCase = true) ||
                    book.publisher.contains(query, ignoreCase = true)

            val matchesGenre = genre == "All" || book.genre.equals(genre, ignoreCase = true)

            val matchesAvail = when (avail) {
                "In Stock" -> book.availableCopies > 2
                "Low Stock" -> book.availableCopies in 1..2
                "Out of Stock" -> book.availableCopies == 0
                else -> true
            }

            matchesQuery && matchesGenre && matchesAvail
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered students
    val filteredStudents: StateFlow<List<Student>> = combine(
        students,
        studentSearchQuery,
        selectedDepartment
    ) { studentList, query, dept ->
        studentList.filter { student ->
            val matchesQuery = query.isBlank() ||
                    student.fullName.contains(query, ignoreCase = true) ||
                    student.studentCode.contains(query, ignoreCase = true) ||
                    student.email.contains(query, ignoreCase = true)

            val matchesDept = dept == "All" || student.department.equals(dept, ignoreCase = true)

            matchesQuery && matchesDept
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Overdue loans
    val overdueLoans: StateFlow<List<Loan>> = loans.combine(MutableStateFlow(Unit)) { loanList, _ ->
        loanList.filter { it.status == LoanStatus.OVERDUE }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active loans for return center
    val activeLoansForReturn: StateFlow<List<Loan>> = loans.combine(MutableStateFlow(Unit)) { loanList, _ ->
        loanList.filter { it.status == LoanStatus.ACTIVE || it.status == LoanStatus.OVERDUE }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            navigationStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (navigationStack.isNotEmpty()) {
            _currentScreen.value = navigationStack.removeAt(navigationStack.lastIndex)
            return true
        }
        return false
    }

    fun login(email: String, role: String) {
        librarian.value = librarian.value.copy(
            email = email.ifBlank { "sarah.vance@libratrack.edu" },
            role = role
        )
        isLoggedIn.value = true
        _currentScreen.value = Screen.DASHBOARD
        showToast("Signed in successfully as ${librarian.value.name}")
    }

    fun logout() {
        isLoggedIn.value = false
        _currentScreen.value = Screen.LOGIN
        showToast("Signed out from administrative terminal")
    }

    fun viewBookDetail(bookId: String) {
        selectedBookId.value = bookId
        navigateTo(Screen.BOOK_DETAIL)
    }

    fun openAddBook() {
        editingBook.value = null
        navigateTo(Screen.ADD_EDIT_BOOK)
    }

    fun openEditBook(book: Book) {
        editingBook.value = book
        navigateTo(Screen.ADD_EDIT_BOOK)
    }

    fun saveBook(
        title: String,
        author: String,
        isbn: String,
        genre: String,
        publisher: String,
        year: Int,
        copies: Int,
        shelf: String,
        desc: String
    ) {
        val currentEdit = editingBook.value
        if (currentEdit == null) {
            val newBook = Book(
                id = "",
                title = title.trim(),
                author = author.trim(),
                isbn = isbn.trim(),
                genre = genre.trim(),
                publisher = publisher.trim(),
                publicationYear = year,
                totalCopies = copies,
                availableCopies = copies,
                shelfLocation = shelf.trim(),
                description = desc.trim(),
                coverGradientIndex = (0..4).random()
            )
            repository.addBook(newBook)
            showToast("Book \"$title\" added to catalog")
        } else {
            val deltaCopies = copies - currentEdit.totalCopies
            val updated = currentEdit.copy(
                title = title.trim(),
                author = author.trim(),
                isbn = isbn.trim(),
                genre = genre.trim(),
                publisher = publisher.trim(),
                publicationYear = year,
                totalCopies = copies,
                availableCopies = (currentEdit.availableCopies + deltaCopies).coerceAtLeast(0),
                shelfLocation = shelf.trim(),
                description = desc.trim()
            )
            repository.updateBook(updated)
            showToast("Book details updated successfully")
        }
        navigateBack()
    }

    fun deleteBook(bookId: String) {
        repository.deleteBook(bookId)
        showToast("Book removed from catalog")
        if (_currentScreen.value == Screen.BOOK_DETAIL) {
            navigateBack()
        }
    }

    fun addStudent(name: String, studentCode: String, email: String, phone: String, dept: String, year: String) {
        val student = Student(
            id = "",
            studentCode = studentCode.trim(),
            fullName = name.trim(),
            email = email.trim(),
            phone = phone.trim(),
            department = dept.trim(),
            yearLevel = year.trim()
        )
        repository.addStudent(student)
        showAddStudentDialog.value = false
        showToast("Student ${student.fullName} registered")
    }

    fun issueLoan() {
        val sId = selectedBorrowStudentId.value
        val bId = selectedBorrowBookId.value
        if (sId == null || bId == null) {
            showToast("Please select both a student and a book")
            return
        }

        val result = repository.borrowBook(
            bookId = bId,
            studentId = sId,
            loanDays = loanDurationDays.value,
            notes = borrowNotes.value
        )

        result.onSuccess {
            showToast("Loan issued! Due in ${loanDurationDays.value} days")
            selectedBorrowBookId.value = null
            borrowNotes.value = ""
        }.onFailure {
            showToast(it.message ?: "Failed to issue loan")
        }
    }

    fun returnBook(loanId: String) {
        repository.returnBook(loanId).onSuccess {
            showToast("Book marked as returned successfully")
        }.onFailure {
            showToast(it.message ?: "Could not return book")
        }
    }

    fun waiveFine(loanId: String) {
        repository.waiveFine(loanId).onSuccess {
            showToast("Fine waived successfully")
        }
    }

    fun sendOverdueReminder(loanId: String) {
        repository.sendOverdueReminder(loanId).onSuccess {
            showToast("Reminder notice dispatched to student")
        }
    }

    fun startBorrowForBook(bookId: String) {
        selectedBorrowBookId.value = bookId
        borrowReturnTab.value = 0
        navigateTo(Screen.BORROW_RETURN)
    }

    fun startBorrowForStudent(studentId: String) {
        selectedBorrowStudentId.value = studentId
        borrowReturnTab.value = 0
        navigateTo(Screen.BORROW_RETURN)
    }

    fun showToast(msg: String) {
        snackbarMessage.value = msg
    }

    fun clearToast() {
        snackbarMessage.value = null
    }
}
