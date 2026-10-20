package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LoanStatus
import com.example.ui.LibraryViewModel
import com.example.ui.components.BadgeType
import com.example.ui.components.StatusBadge
import com.example.ui.components.TableHeader
import com.example.ui.components.TableHeaderCell
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.DangerRed
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.Navy700
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun BookDetailScreen(
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier
) {
    val books by viewModel.books.collectAsState()
    val loans by viewModel.loans.collectAsState()
    val selectedId by viewModel.selectedBookId.collectAsState()

    val book = books.find { it.id == selectedId } ?: books.firstOrNull()

    if (book == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Book not found")
        }
        return
    }

    val bookHistory = loans.filter { it.bookId == book.id }

    val gradients = listOf(
        listOf(Color(0xFF1E3A8A), Color(0xFF3B82F6)),
        listOf(Color(0xFF0F766E), Color(0xFF14B8A6)),
        listOf(Color(0xFFB45309), Color(0xFFF59E0B)),
        listOf(Color(0xFF6D28D9), Color(0xFF8B5CF6)),
        listOf(Color(0xFF831843), Color(0xFFEC4899))
    )
    val coverColors = gradients[book.coverGradientIndex % gradients.size]

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Top Back Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("detail_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Navy700
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Catalog Details",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { viewModel.openEditBook(book) },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("edit_book_btn")
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Edit Book", fontSize = 13.sp)
                    }

                    if (book.availableCopies > 0) {
                        Button(
                            onClick = { viewModel.startBorrowForBook(book.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = Navy700),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("issue_book_btn")
                        ) {
                            Icon(Icons.Default.SwapHoriz, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Issue Loan", fontSize = 13.sp)
                        }
                    }

                    IconButton(
                        onClick = { viewModel.deleteBook(book.id) },
                        modifier = Modifier.testTag("delete_book_btn")
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = DangerRed)
                    }
                }
            }
        }

        // Hero Cover & Metadata Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, BorderSubtle)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // Cover Spine Artwork
                    Box(
                        modifier = Modifier
                            .size(width = 110.dp, height = 160.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Brush.verticalGradient(coverColors))
                            .padding(12.dp),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Column {
                            Text(
                                text = book.genre.uppercase(),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 9.sp,
                                    letterSpacing = 1.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = book.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 14.sp
                                ),
                                maxLines = 3
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(24.dp))

                    // Metadata details
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StatusBadge(text = book.genre, type = BadgeType.INFO)
                            when {
                                book.availableCopies == 0 -> StatusBadge("Out of Stock", BadgeType.DANGER)
                                book.availableCopies <= 2 -> StatusBadge("Low Stock (${book.availableCopies} left)", BadgeType.WARNING)
                                else -> StatusBadge("Available (${book.availableCopies} in stock)", BadgeType.SUCCESS)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = book.title,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                fontSize = 22.sp
                            )
                        )

                        Text(
                            text = "by ${book.author}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = TextSecondary,
                                fontSize = 15.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Quick info grid
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            Column {
                                Text("ISBN-13", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                                Text(book.isbn, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                            }
                            Column {
                                Text("PUBLISHER", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                                Text("${book.publisher} (${book.publicationYear})", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                            }
                            Column {
                                Text("SHELF LOCATION", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Place, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(book.shelfLocation, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                                }
                            }
                        }

                        if (book.description.isNotBlank()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = book.description,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = TextSecondary,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        // Borrow History Section Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.History, contentDescription = null, tint = Navy700)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Borrow History for This Book",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 16.sp
                        )
                    )
                }

                Text(
                    text = "${bookHistory.size} total loan transactions recorded",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                )
            }
        }

        // Borrow History Table
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, BorderSubtle)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    TableHeader {
                        TableHeaderCell("Borrower Student", modifier = Modifier.weight(1.5f))
                        TableHeaderCell("Issued Date", modifier = Modifier.weight(1f))
                        TableHeaderCell("Due Date", modifier = Modifier.weight(1f))
                        TableHeaderCell("Returned Date", modifier = Modifier.weight(1f))
                        TableHeaderCell("Status", modifier = Modifier.weight(1f))
                    }

                    if (bookHistory.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No borrow records found for this title yet.",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                            )
                        }
                    } else {
                        bookHistory.forEach { loan ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1.5f)) {
                                    Text(
                                        text = loan.studentName,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextPrimary,
                                            fontSize = 13.sp
                                        )
                                    )
                                    Text(
                                        text = loan.studentCode,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextMuted,
                                            fontSize = 11.sp
                                        )
                                    )
                                }

                                Text(
                                    text = loan.borrowDate,
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                                    modifier = Modifier.weight(1f)
                                )

                                Text(
                                    text = loan.dueDate,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = if (loan.status == LoanStatus.OVERDUE) DangerRed else TextSecondary,
                                        fontWeight = if (loan.status == LoanStatus.OVERDUE) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    modifier = Modifier.weight(1f)
                                )

                                Text(
                                    text = loan.returnDate ?: "—",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                                    modifier = Modifier.weight(1f)
                                )

                                Box(modifier = Modifier.weight(1f)) {
                                    when (loan.status) {
                                        LoanStatus.ACTIVE -> StatusBadge("Active", BadgeType.INFO)
                                        LoanStatus.RETURNED -> StatusBadge("Returned", BadgeType.SUCCESS)
                                        LoanStatus.OVERDUE -> StatusBadge("Overdue", BadgeType.DANGER)
                                    }
                                }
                            }
                            HorizontalDivider(color = BorderSubtle, thickness = 0.5.dp)
                        }
                    }
                }
            }
        }
    }
}
