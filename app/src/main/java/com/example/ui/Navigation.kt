package com.example.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screen(
    val title: String,
    val icon: ImageVector? = null,
    val showInSidebar: Boolean = true
) {
    LOGIN("Sign In", null, showInSidebar = false),
    DASHBOARD("Dashboard", Icons.Default.Dashboard),
    CATALOG("Book Catalog", Icons.Default.Book),
    STUDENTS("Students", Icons.Default.People),
    BORROW_RETURN("Borrow / Return", Icons.Default.SwapHoriz),
    OVERDUE("Overdue List", Icons.Default.Warning),
    REPORTS("Reports & Analytics", Icons.Default.Assessment),
    BOOK_DETAIL("Book Detail", null, showInSidebar = false),
    ADD_EDIT_BOOK("Add / Edit Book", null, showInSidebar = false)
}
