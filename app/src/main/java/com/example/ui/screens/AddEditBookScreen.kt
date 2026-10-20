package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.LibraryViewModel
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.Navy700
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AddEditBookScreen(
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier
) {
    val editingBook by viewModel.editingBook.collectAsState()

    var title by remember(editingBook) { mutableStateOf(editingBook?.title ?: "") }
    var author by remember(editingBook) { mutableStateOf(editingBook?.author ?: "") }
    var isbn by remember(editingBook) { mutableStateOf(editingBook?.isbn ?: "") }
    var genre by remember(editingBook) { mutableStateOf(editingBook?.genre ?: "Computer Science") }
    var publisher by remember(editingBook) { mutableStateOf(editingBook?.publisher ?: "") }
    var yearStr by remember(editingBook) { mutableStateOf(editingBook?.publicationYear?.toString() ?: "2024") }
    var copies by remember(editingBook) { mutableIntStateOf(editingBook?.totalCopies ?: 5) }
    var shelfLocation by remember(editingBook) { mutableStateOf(editingBook?.shelfLocation ?: "Floor 2 • Aisle 4") }
    var description by remember(editingBook) { mutableStateOf(editingBook?.description ?: "") }
    var selectedGradientIndex by remember(editingBook) { mutableIntStateOf(editingBook?.coverGradientIndex ?: 0) }

    val isEditing = editingBook != null
    val scrollState = rememberScrollState()

    val coverPaletteGradients = listOf(
        listOf(Color(0xFF1E3A8A), Color(0xFF3B82F6)), // Navy
        listOf(Color(0xFF0F766E), Color(0xFF14B8A6)), // Teal
        listOf(Color(0xFFB45309), Color(0xFFF59E0B)), // Amber
        listOf(Color(0xFF6D28D9), Color(0xFF8B5CF6)), // Purple
        listOf(Color(0xFF831843), Color(0xFFEC4899))  // Rose
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 20.dp)
            .verticalScroll(scrollState)
    ) {
        // Top Navigation Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = { viewModel.navigateBack() },
                modifier = Modifier.testTag("back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Navy700
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = if (isEditing) "Edit Catalog Entry" else "Register New Book",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 22.sp
                    )
                )
                Text(
                    text = if (isEditing) "Update metadata and stock holdings" else "Add a new title to the institutional catalog",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Main Form Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 800.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Book Cover Theme / Upload Selection
                Column {
                    Text(
                        text = "Book Cover Style & Theme",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        coverPaletteGradients.forEachIndexed { index, gradient ->
                            val isSelected = selectedGradientIndex == index
                            Box(
                                modifier = Modifier
                                    .size(width = 44.dp, height = 58.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Brush.verticalGradient(gradient))
                                    .clickable { selectedGradientIndex = index }
                                    .then(
                                        if (isSelected) {
                                            Modifier.border(2.dp, GoldAccent, RoundedCornerShape(6.dp))
                                        } else Modifier
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        OutlinedButton(
                            onClick = {
                                viewModel.showToast("Cover asset uploaded & optimized")
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("upload_cover_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Image, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Upload Asset", fontSize = 12.sp)
                        }
                    }
                }

                // Title field
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Book Title *") },
                    leadingIcon = { Icon(Icons.Default.Book, contentDescription = null, tint = Navy700) },
                    modifier = Modifier.fillMaxWidth().testTag("book_title_input"),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                // Author & ISBN Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    OutlinedTextField(
                        value = author,
                        onValueChange = { author = it },
                        label = { Text("Author(s) *") },
                        modifier = Modifier.weight(1f).testTag("book_author_input"),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = isbn,
                        onValueChange = { isbn = it },
                        label = { Text("ISBN-13 *") },
                        modifier = Modifier.weight(1f).testTag("book_isbn_input"),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                }

                // Genre & Publisher Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    OutlinedTextField(
                        value = genre,
                        onValueChange = { genre = it },
                        label = { Text("Genre / Category *") },
                        modifier = Modifier.weight(1f).testTag("book_genre_input"),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = publisher,
                        onValueChange = { publisher = it },
                        label = { Text("Publisher") },
                        modifier = Modifier.weight(1f).testTag("book_publisher_input"),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                }

                // Publication Year, Total Copies, Shelf Location
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = yearStr,
                        onValueChange = { yearStr = it },
                        label = { Text("Publication Year") },
                        modifier = Modifier.weight(1f).testTag("book_year_input"),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    // Total Copies Counter
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Total Copies: $copies",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = TextSecondary
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            IconButton(
                                onClick = { if (copies > 1) copies-- },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFF1F5F9))
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(16.dp))
                            }

                            Text(
                                text = "$copies",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )

                            IconButton(
                                onClick = { copies++ },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFF1F5F9))
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(16.dp))
                            }
                        }
                    }

                    OutlinedTextField(
                        value = shelfLocation,
                        onValueChange = { shelfLocation = it },
                        label = { Text("Shelf Location") },
                        modifier = Modifier.weight(1.2f).testTag("book_shelf_input"),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                }

                // Description field
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Book Summary & Catalog Notes") },
                    modifier = Modifier.fillMaxWidth().testTag("book_desc_input"),
                    shape = RoundedCornerShape(10.dp),
                    minLines = 3,
                    maxLines = 5
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Actions: Save & Cancel
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { viewModel.navigateBack() },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("cancel_button")
                    ) {
                        Text("Cancel", color = TextSecondary)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = {
                            if (title.isBlank() || author.isBlank()) {
                                viewModel.showToast("Title and Author are required")
                                return@Button
                            }
                            val year = yearStr.toIntOrNull() ?: 2024
                            viewModel.saveBook(
                                title = title,
                                author = author,
                                isbn = isbn.ifBlank { "978-0" + (100000000..999999999).random() },
                                genre = genre,
                                publisher = publisher.ifBlank { "Academic Press" },
                                year = year,
                                copies = copies,
                                shelf = shelfLocation,
                                desc = description
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Navy700),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("save_book_button")
                    ) {
                        Text(if (isEditing) "Save Changes" else "Save Book to Catalog", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}
