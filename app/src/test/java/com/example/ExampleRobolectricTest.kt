package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.LibraryRepository
import com.example.model.LoanStatus
import com.example.ui.LibraryViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context matches LibraTrack`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("LibraTrack", appName)
    }

    @Test
    fun `repository loads initial data and processes borrow and return correctly`() {
        val repo = LibraryRepository()
        val initialBooks = repo.books.value
        val initialStudents = repo.students.value
        val initialLoans = repo.loans.value

        assertTrue(initialBooks.isNotEmpty())
        assertTrue(initialStudents.isNotEmpty())
        assertTrue(initialLoans.isNotEmpty())

        val availableBook = initialBooks.first { it.availableCopies > 0 }
        val student = initialStudents.first()
        val initialAvailCopies = availableBook.availableCopies

        // Issue a loan
        val borrowResult = repo.borrowBook(
            bookId = availableBook.id,
            studentId = student.id,
            loanDays = 14
        )
        assertTrue(borrowResult.isSuccess)
        val loan = borrowResult.getOrNull()
        assertNotNull(loan)
        assertEquals(LoanStatus.ACTIVE, loan?.status)

        // Book copies should decrement by 1
        val updatedBook = repo.books.value.first { it.id == availableBook.id }
        assertEquals(initialAvailCopies - 1, updatedBook.availableCopies)

        // Process Return
        val returnResult = repo.returnBook(loan!!.id)
        assertTrue(returnResult.isSuccess)

        // Book copies should restore
        val returnedBook = repo.books.value.first { it.id == availableBook.id }
        assertEquals(initialAvailCopies, returnedBook.availableCopies)
    }

    @Test
    fun `waive fine removes overdue penalty`() {
        val repo = LibraryRepository()
        val overdueLoan = repo.loans.value.first { it.status == LoanStatus.OVERDUE }
        assertTrue(overdueLoan.fineAmount > 0.0)

        val result = repo.waiveFine(overdueLoan.id)
        assertTrue(result.isSuccess)

        val updated = repo.loans.value.first { it.id == overdueLoan.id }
        assertEquals(0.0, updated.fineAmount, 0.001)
        assertTrue(updated.fineSettled)
    }
}
