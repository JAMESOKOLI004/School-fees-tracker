package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.LibraryViewModel
import com.example.ui.components.BadgeType
import com.example.ui.components.BookCoverThumbnail
import com.example.ui.components.StatusBadge
import com.example.ui.components.TableHeader
import com.example.ui.components.TableHeaderCell
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.Navy700
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun BookCatalogScreen(
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier
) {
    val books by viewModel.filteredBooks.collectAsState()
    val searchQuery by viewModel.catalogSearchQuery.collectAsState()
    val selectedGenre by viewModel.selectedGenre.collectAsState()
    val selectedAvailability by viewModel.selectedAvailability.collectAsState()

    val genres = listOf("All", "Computer Science", "System Design", "Software Engineering", "Artificial Intelligence", "Literature", "History")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        // Top Action Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Book Catalog",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 24.sp
                    )
                )
                Text(
                    text = "${books.size} titles currently registered in library collection",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                )
            }

            Button(
                onClick = { viewModel.openAddBook() },
                colors = ButtonDefaults.buttonColors(containerColor = Navy700),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("add_book_button")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Add Book", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search & Filters Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.catalogSearchQuery.value = it },
                placeholder = { Text("Search by title, author, ISBN...", fontSize = 13.sp) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = Navy700)
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { viewModel.catalogSearchQuery.value = "" }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", modifier = Modifier.size(18.dp))
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("catalog_search_input"),
                shape = RoundedCornerShape(10.dp),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Genre Filter Tabs
        ScrollableTabRow(
            selectedTabIndex = genres.indexOf(selectedGenre).coerceAtLeast(0),
            edgePadding = 0.dp,
            containerColor = Color.Transparent,
            divider = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            genres.forEach { genre ->
                val isSelected = selectedGenre == genre
                Tab(
                    selected = isSelected,
                    onClick = { viewModel.selectedGenre.value = genre },
                    text = {
                        Text(
                            text = genre,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Navy700 else TextSecondary
                            )
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Data Table Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, BorderSubtle)
        ) {
            val tableScrollState = rememberScrollState()

            Column(modifier = Modifier.fillMaxSize()) {
                // Horizontal scrolling container for tablet / desktop data-table
                Box(modifier = Modifier.horizontalScroll(tableScrollState)) {
                    Column(modifier = Modifier.width(960.dp)) {
                        // Table Header
                        TableHeader {
                            TableHeaderCell("Book Title & Author", modifier = Modifier.width(320.dp))
                            TableHeaderCell("ISBN", modifier = Modifier.width(160.dp))
                            TableHeaderCell("Genre", modifier = Modifier.width(150.dp))
                            TableHeaderCell("Copies", modifier = Modifier.width(110.dp))
                            TableHeaderCell("Status", modifier = Modifier.width(110.dp))
                            TableHeaderCell("Actions", modifier = Modifier.width(110.dp))
                        }

                        // Table Rows
                        if (books.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(260.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = TextMuted,
                                        modifier = Modifier.size(40.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "No books match your criteria",
                                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                                    )
                                    Text(
                                        text = "Try clearing your search terms or genre filter",
                                        style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                                    )
                                }
                            }
                        } else {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                items(books, key = { it.id }) { book ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { viewModel.viewBookDetail(book.id) }
                                            .padding(horizontal = 16.dp, vertical = 10.dp)
                                            .testTag("book_row_${book.id}"),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Title & Author with cover thumbnail
                                        Row(
                                            modifier = Modifier.width(320.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            BookCoverThumbnail(
                                                title = book.title,
                                                gradientIndex = book.coverGradientIndex
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column {
                                                Text(
                                                    text = book.title,
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = TextPrimary,
                                                        fontSize = 13.sp
                                                    ),
                                                    maxLines = 1
                                                )
                                                Text(
                                                    text = book.author,
                                                    style = MaterialTheme.typography.bodySmall.copy(
                                                        color = TextSecondary,
                                                        fontSize = 12.sp
                                                    ),
                                                    maxLines = 1
                                                )
                                            }
                                        }

                                        // ISBN
                                        Text(
                                            text = book.isbn,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = TextSecondary,
                                                fontSize = 12.sp
                                            ),
                                            modifier = Modifier.width(160.dp)
                                        )

                                        // Genre
                                        Box(modifier = Modifier.width(150.dp)) {
                                            StatusBadge(text = book.genre, type = BadgeType.INFO)
                                        }

                                        // Copies
                                        Text(
                                            text = "${book.availableCopies} / ${book.totalCopies}",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.SemiBold,
                                                color = if (book.availableCopies == 0) Color(0xFFDC2626) else TextPrimary,
                                                fontSize = 13.sp
                                            ),
                                            modifier = Modifier.width(110.dp)
                                        )

                                        // Status Badge
                                        Box(modifier = Modifier.width(110.dp)) {
                                            when {
                                                book.availableCopies == 0 -> StatusBadge("Out of Stock", BadgeType.DANGER)
                                                book.availableCopies <= 2 -> StatusBadge("Low Stock", BadgeType.WARNING)
                                                else -> StatusBadge("In Stock", BadgeType.SUCCESS)
                                            }
                                        }

                                        // Actions
                                        Row(
                                            modifier = Modifier.width(110.dp),
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            IconButton(
                                                onClick = { viewModel.viewBookDetail(book.id) },
                                                modifier = Modifier.size(32.dp).testTag("view_book_${book.id}")
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Info,
                                                    contentDescription = "View Details",
                                                    tint = Navy700,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }

                                            IconButton(
                                                onClick = { viewModel.openEditBook(book) },
                                                modifier = Modifier.size(32.dp).testTag("edit_book_${book.id}")
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Edit,
                                                    contentDescription = "Edit Book",
                                                    tint = Color(0xFF475569),
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }

                                            if (book.availableCopies > 0) {
                                                IconButton(
                                                    onClick = { viewModel.startBorrowForBook(book.id) },
                                                    modifier = Modifier.size(32.dp).testTag("borrow_book_${book.id}")
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.SwapHoriz,
                                                        contentDescription = "Borrow",
                                                        tint = Color(0xFF059669),
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                }
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
    }
}
