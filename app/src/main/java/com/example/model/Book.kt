package com.example.model

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val isbn: String,
    val genre: String,
    val publisher: String,
    val publicationYear: Int,
    val totalCopies: Int,
    val availableCopies: Int,
    val shelfLocation: String,
    val description: String = "",
    val coverGradientIndex: Int = 0
) {
    val isAvailable: Boolean
        get() = availableCopies > 0

    val statusText: String
        get() = when {
            availableCopies == 0 -> "Out of Stock"
            availableCopies <= 2 -> "Low Stock"
            else -> "In Stock"
        }
}
