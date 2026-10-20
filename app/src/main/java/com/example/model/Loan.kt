package com.example.model

enum class LoanStatus {
    ACTIVE,
    RETURNED,
    OVERDUE
}

data class Loan(
    val id: String,
    val bookId: String,
    val bookTitle: String,
    val bookIsbn: String,
    val studentId: String,
    val studentName: String,
    val studentCode: String,
    val borrowDate: String,
    val dueDate: String,
    val returnDate: String? = null,
    val status: LoanStatus = LoanStatus.ACTIVE,
    val daysOverdue: Int = 0,
    val fineAmount: Double = 0.0,
    val fineSettled: Boolean = false,
    val notes: String = ""
)
