package com.example.data

import com.example.model.ActivityLog
import com.example.model.ActivityType
import com.example.model.Book
import com.example.model.Loan
import com.example.model.LoanStatus
import com.example.model.Student
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

class LibraryRepository {

    private val _books = MutableStateFlow<List<Book>>(LibraryDataSeeder.getInitialBooks())
    val books: StateFlow<List<Book>> = _books.asStateFlow()

    private val _students = MutableStateFlow<List<Student>>(LibraryDataSeeder.getInitialStudents())
    val students: StateFlow<List<Student>> = _students.asStateFlow()

    private val _loans = MutableStateFlow<List<Loan>>(LibraryDataSeeder.getInitialLoans())
    val loans: StateFlow<List<Loan>> = _loans.asStateFlow()

    private val _activities = MutableStateFlow<List<ActivityLog>>(LibraryDataSeeder.getInitialActivities())
    val activities: StateFlow<List<ActivityLog>> = _activities.asStateFlow()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun addBook(book: Book) {
        val newBook = if (book.id.isBlank()) book.copy(id = UUID.randomUUID().toString()) else book
        _books.value = listOf(newBook) + _books.value

        logActivity(
            type = ActivityType.BOOK_ADDED,
            title = "New Book Cataloged",
            description = "\"${newBook.title}\" added (${newBook.totalCopies} copies in ${newBook.genre})"
        )
    }

    fun updateBook(book: Book) {
        _books.value = _books.value.map { if (it.id == book.id) book else it }
    }

    fun deleteBook(bookId: String) {
        val target = _books.value.find { it.id == bookId }
        _books.value = _books.value.filterNot { it.id == bookId }
        if (target != null) {
            logActivity(
                type = ActivityType.BOOK_ADDED,
                title = "Book Removed",
                description = "\"${target.title}\" was uncataloged from database"
            )
        }
    }

    fun addStudent(student: Student) {
        val newStudent = if (student.id.isBlank()) student.copy(id = UUID.randomUUID().toString()) else student
        _students.value = listOf(newStudent) + _students.value

        logActivity(
            type = ActivityType.STUDENT_ADDED,
            title = "Student Registered",
            description = "${newStudent.fullName} (${newStudent.studentCode}) • ${newStudent.department}"
        )
    }

    fun updateStudent(student: Student) {
        _students.value = _students.value.map { if (it.id == student.id) student else it }
    }

    fun borrowBook(bookId: String, studentId: String, loanDays: Int = 14, notes: String = ""): Result<Loan> {
        val currentBook = _books.value.find { it.id == bookId }
            ?: return Result.failure(Exception("Book not found"))
        if (currentBook.availableCopies <= 0) {
            return Result.failure(Exception("No copies available for loan"))
        }

        val currentStudent = _students.value.find { it.id == studentId }
            ?: return Result.failure(Exception("Student not found"))

        val cal = Calendar.getInstance()
        val todayStr = dateFormat.format(cal.time)
        cal.add(Calendar.DAY_OF_YEAR, loanDays)
        val dueDateStr = dateFormat.format(cal.time)

        val newLoan = Loan(
            id = "loan-" + UUID.randomUUID().toString().take(6),
            bookId = currentBook.id,
            bookTitle = currentBook.title,
            bookIsbn = currentBook.isbn,
            studentId = currentStudent.id,
            studentName = currentStudent.fullName,
            studentCode = currentStudent.studentCode,
            borrowDate = todayStr,
            dueDate = dueDateStr,
            status = LoanStatus.ACTIVE,
            notes = notes
        )

        // Decrement book available copies
        _books.value = _books.value.map {
            if (it.id == bookId) it.copy(availableCopies = (it.availableCopies - 1).coerceAtLeast(0)) else it
        }

        // Increment student active loans
        _students.value = _students.value.map {
            if (it.id == studentId) {
                it.copy(
                    activeLoansCount = it.activeLoansCount + 1,
                    totalLoansHistory = it.totalLoansHistory + 1
                )
            } else it
        }

        // Prepend loan
        _loans.value = listOf(newLoan) + _loans.value

        logActivity(
            type = ActivityType.BORROW,
            title = "Book Issued: ${currentBook.title}",
            description = "Issued to ${currentStudent.fullName} • Due $dueDateStr"
        )

        return Result.success(newLoan)
    }

    fun returnBook(loanId: String): Result<Unit> {
        val loan = _loans.value.find { it.id == loanId }
            ?: return Result.failure(Exception("Loan not found"))

        val todayStr = dateFormat.format(Date())

        _loans.value = _loans.value.map {
            if (it.id == loanId) {
                it.copy(
                    status = LoanStatus.RETURNED,
                    returnDate = todayStr,
                    fineSettled = true
                )
            } else it
        }

        // Increment book available copies
        _books.value = _books.value.map {
            if (it.id == loan.bookId) {
                it.copy(availableCopies = (it.availableCopies + 1).coerceAtMost(it.totalCopies))
            } else it
        }

        // Update student active loan count & unpaid fines if settled
        _students.value = _students.value.map {
            if (it.id == loan.studentId) {
                val fineDeduction = if (loan.status == LoanStatus.OVERDUE) loan.fineAmount else 0.0
                it.copy(
                    activeLoansCount = (it.activeLoansCount - 1).coerceAtLeast(0),
                    unpaidFineAmount = (it.unpaidFineAmount - fineDeduction).coerceAtLeast(0.0)
                )
            } else it
        }

        logActivity(
            type = ActivityType.RETURN,
            title = "Book Returned: ${loan.bookTitle}",
            description = "Returned by ${loan.studentName} (${loan.studentCode})"
        )

        return Result.success(Unit)
    }

    fun waiveFine(loanId: String): Result<Unit> {
        val loan = _loans.value.find { it.id == loanId }
            ?: return Result.failure(Exception("Loan not found"))

        _loans.value = _loans.value.map {
            if (it.id == loanId) it.copy(fineAmount = 0.0, fineSettled = true) else it
        }

        _students.value = _students.value.map {
            if (it.id == loan.studentId) {
                it.copy(unpaidFineAmount = (it.unpaidFineAmount - loan.fineAmount).coerceAtLeast(0.0))
            } else it
        }

        logActivity(
            type = ActivityType.FINE_COLLECTED,
            title = "Fine Waived",
            description = "Librarian waived \$${String.format(Locale.US, "%.2f", loan.fineAmount)} fine for ${loan.studentName}"
        )

        return Result.success(Unit)
    }

    fun sendOverdueReminder(loanId: String): Result<Unit> {
        val loan = _loans.value.find { it.id == loanId }
            ?: return Result.failure(Exception("Loan not found"))

        logActivity(
            type = ActivityType.OVERDUE_ALERT,
            title = "Reminder Notice Sent",
            description = "Dispatched notification to ${loan.studentName} for \"${loan.bookTitle}\""
        )
        return Result.success(Unit)
    }

    fun getBookLoansHistory(bookId: String): List<Loan> {
        return _loans.value.filter { it.bookId == bookId }
    }

    private fun logActivity(type: ActivityType, title: String, description: String) {
        val newLog = ActivityLog(
            id = "log-" + UUID.randomUUID().toString().take(6),
            type = type,
            title = title,
            description = description,
            timestamp = "Just now"
        )
        _activities.value = listOf(newLog) + _activities.value.take(19)
    }
}
