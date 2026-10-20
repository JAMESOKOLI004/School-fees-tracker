package com.example.model

data class Student(
    val id: String,
    val studentCode: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val department: String,
    val yearLevel: String,
    val activeLoansCount: Int = 0,
    val totalLoansHistory: Int = 0,
    val unpaidFineAmount: Double = 0.0
)
