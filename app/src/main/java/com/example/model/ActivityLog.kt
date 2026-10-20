package com.example.model

enum class ActivityType {
    BORROW,
    RETURN,
    OVERDUE_ALERT,
    BOOK_ADDED,
    STUDENT_ADDED,
    FINE_COLLECTED
}

data class ActivityLog(
    val id: String,
    val type: ActivityType,
    val title: String,
    val description: String,
    val timestamp: String
)

data class LibrarianUser(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val badgeNumber: String
)
