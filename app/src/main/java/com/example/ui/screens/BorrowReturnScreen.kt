package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LoanStatus
import com.example.ui.LibraryViewModel
import com.example.ui.components.BadgeType
import com.example.ui.components.BookCoverThumbnail
import com.example.ui.components.StatusBadge
import com.example.ui.components.TableHeader
import com.example.ui.components.TableHeaderCell
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.DangerRed
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.Navy700
import com.example.ui.theme.SuccessContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BorrowReturnScreen(
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier
) {
    val currentTab by viewModel.borrowReturnTab.collectAsState()
    val books by viewModel.books.collectAsState()
    val students by viewModel.students.collectAsState()
    val activeLoans by viewModel.activeLoansForReturn.collectAsState()

    val selectedStudentId by viewModel.selectedBorrowStudentId.collectAsState()
    val selectedBookId by viewModel.selectedBorrowBookId.collectAsState()
    val loanDays by viewModel.loanDurationDays.collectAsState()
    val borrowNotes by viewModel.borrowNotes.collectAsState()

    val selectedStudent = students.find { it.id == selectedStudentId }
    val selectedBook = books.find { it.id == selectedBookId }

    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }
    val cal = remember(loanDays) {
        val c = Calendar.getInstance()
        val today = dateFormat.format(c.time)
        c.add(Calendar.DAY_OF_YEAR, loanDays)
        val due = dateFormat.format(c.time)
        Pair(today, due)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        // Header
        Column {
            Text(
                text = "Circulation Desk",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    fontSize = 24.sp
                )
            )
            Text(
                text = "Issue new loans with calculated due dates or process returns in real-time",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tab Selector (Issue vs Return)
        TabRow(
            selectedTabIndex = currentTab,
            containerColor = SurfaceCard,
            contentColor = Navy700,
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, BorderSubtle, RoundedCornerShape(10.dp))
        ) {
            Tab(
                selected = currentTab == 0,
                onClick = { viewModel.borrowReturnTab.value = 0 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.SwapHoriz, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Issue New Loan (Borrow)", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }
                },
                modifier = Modifier.testTag("tab_issue_loan")
            )
            Tab(
                selected = currentTab == 1,
                onClick = { viewModel.borrowReturnTab.value = 1 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Return Desk (${activeLoans.size} Active)", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }
                },
                modifier = Modifier.testTag("tab_return_desk")
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        if (currentTab == 0) {
            // ISSUE LOAN TAB
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // Left Column: Student & Book Picker Form
                Card(
                    modifier = Modifier.weight(1.3f),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, BorderSubtle)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Loan Particulars",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )

                        // Student Selector Dropdown
                        var studentExpanded by remember { mutableStateOf(false) }
                        var studentFilter by remember { mutableStateOf("") }
                        val matchingStudents = students.filter {
                            studentFilter.isBlank() ||
                                    it.fullName.contains(studentFilter, ignoreCase = true) ||
                                    it.studentCode.contains(studentFilter, ignoreCase = true)
                        }

                        ExposedDropdownMenuBox(
                            expanded = studentExpanded,
                            onExpandedChange = { studentExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = selectedStudent?.let { "${it.fullName} (${it.studentCode})" } ?: studentFilter,
                                onValueChange = {
                                    studentFilter = it
                                    studentExpanded = true
                                },
                                label = { Text("Select Student Patron *") },
                                placeholder = { Text("Search by student name or ID...") },
                                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Navy700) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = studentExpanded) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                                    .testTag("borrow_student_picker"),
                                shape = RoundedCornerShape(10.dp)
                            )

                            ExposedDropdownMenu(
                                expanded = studentExpanded,
                                onDismissRequest = { studentExpanded = false }
                            ) {
                                matchingStudents.forEach { student ->
                                    DropdownMenuItem(
                                        text = {
                                            Column {
                                                Text(
                                                    text = "${student.fullName} • ${student.studentCode}",
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 13.sp
                                                )
                                                Text(
                                                    text = "${student.department} • ${student.activeLoansCount} active loans",
                                                    color = TextSecondary,
                                                    fontSize = 11.sp
                                                )
                                            }
                                        },
                                        onClick = {
                                            viewModel.selectedBorrowStudentId.value = student.id
                                            studentFilter = ""
                                            studentExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Book Selector Dropdown
                        var bookExpanded by remember { mutableStateOf(false) }
                        var bookFilter by remember { mutableStateOf("") }
                        val availableBooks = books.filter { it.availableCopies > 0 && (bookFilter.isBlank() || it.title.contains(bookFilter, ignoreCase = true) || it.author.contains(bookFilter, ignoreCase = true)) }

                        ExposedDropdownMenuBox(
                            expanded = bookExpanded,
                            onExpandedChange = { bookExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = selectedBook?.let { "${it.title} (${it.availableCopies} available)" } ?: bookFilter,
                                onValueChange = {
                                    bookFilter = it
                                    bookExpanded = true
                                },
                                label = { Text("Select Book from Catalog *") },
                                placeholder = { Text("Search by book title or author...") },
                                leadingIcon = { Icon(Icons.Default.Book, contentDescription = null, tint = Navy700) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = bookExpanded) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                                    .testTag("borrow_book_picker"),
                                shape = RoundedCornerShape(10.dp)
                            )

                            ExposedDropdownMenu(
                                expanded = bookExpanded,
                                onDismissRequest = { bookExpanded = false }
                            ) {
                                availableBooks.forEach { book ->
                                    DropdownMenuItem(
                                        text = {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                BookCoverThumbnail(title = book.title, gradientIndex = book.coverGradientIndex)
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Column {
                                                    Text(text = book.title, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                                    Text(text = "${book.author} • ${book.availableCopies} available", color = TextSecondary, fontSize = 11.sp)
                                                }
                                            }
                                        },
                                        onClick = {
                                            viewModel.selectedBorrowBookId.value = book.id
                                            bookFilter = ""
                                            bookExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Loan Duration Selector
                        Column {
                            Text(
                                text = "Loan Duration Period",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = TextSecondary)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf(7 to "7 Days", 14 to "14 Days (Standard)", 28 to "28 Days").forEach { (days, label) ->
                                    val isSelected = loanDays == days
                                    OutlinedButton(
                                        onClick = { viewModel.loanDurationDays.value = days },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = if (isSelected) Navy700 else Color.Transparent,
                                            contentColor = if (isSelected) Color.White else TextPrimary
                                        ),
                                        modifier = Modifier.weight(1f).testTag("duration_${days}_days")
                                    ) {
                                        Text(text = label, fontSize = 11.sp, maxLines = 1)
                                    }
                                }
                            }
                        }

                        // Internal Notes
                        OutlinedTextField(
                            value = borrowNotes,
                            onValueChange = { viewModel.borrowNotes.value = it },
                            label = { Text("Circulation Notes (Optional)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )
                    }
                }

                // Right Column: Live Calculated Due Date & Issue Action
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, BorderSubtle)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Loan Calculation Summary",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )

                        // Auto-Calculated Date Card
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(GoldContainer)
                                .padding(16.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.CalendarToday, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Calculated Return Due Date",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF92400E)
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = cal.second,
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary,
                                        fontSize = 22.sp
                                    )
                                )
                                Text(
                                    text = "Issued: ${cal.first} (${loanDays} days grace period)",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFF78350F),
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }

                        // Summary Check items
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Borrower:", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                                Text(
                                    text = selectedStudent?.fullName ?: "None selected",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Book Title:", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                                Text(
                                    text = selectedBook?.title ?: "None selected",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                    maxLines = 1
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Daily Overdue Fine Rate:", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                                Text("\$0.50 / day", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = GoldAccent))
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Button(
                            onClick = { viewModel.issueLoan() },
                            colors = ButtonDefaults.buttonColors(containerColor = Navy700),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("confirm_issue_loan_button"),
                            enabled = selectedStudent != null && selectedBook != null
                        ) {
                            Icon(Icons.Default.SwapHoriz, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Confirm & Issue Loan", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }
        } else {
            // RETURN DESK TAB
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, BorderSubtle)
            ) {
                val scrollState = rememberScrollState()

                Column(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.horizontalScroll(scrollState)) {
                        Column(modifier = Modifier.width(960.dp)) {
                            TableHeader {
                                TableHeaderCell("Borrower Student", modifier = Modifier.width(260.dp))
                                TableHeaderCell("Borrowed Book", modifier = Modifier.width(300.dp))
                                TableHeaderCell("Issued Date", modifier = Modifier.width(130.dp))
                                TableHeaderCell("Due Date / Status", modifier = Modifier.width(140.dp))
                                TableHeaderCell("Action", modifier = Modifier.width(130.dp))
                            }

                            if (activeLoans.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(240.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("No currently borrowed books to return.", color = TextMuted)
                                }
                            } else {
                                LazyColumn(modifier = Modifier.fillMaxSize()) {
                                    items(activeLoans, key = { it.id }) { loan ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp, vertical = 12.dp)
                                                .testTag("loan_row_${loan.id}"),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            // Student
                                            Column(modifier = Modifier.width(260.dp)) {
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
                                                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted, fontSize = 11.sp)
                                                )
                                            }

                                            // Book Title
                                            Column(modifier = Modifier.width(300.dp)) {
                                                Text(
                                                    text = loan.bookTitle,
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = TextPrimary,
                                                        fontSize = 13.sp
                                                    ),
                                                    maxLines = 1
                                                )
                                                Text(
                                                    text = loan.bookIsbn,
                                                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted, fontSize = 11.sp)
                                                )
                                            }

                                            // Borrow Date
                                            Text(
                                                text = loan.borrowDate,
                                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                                                modifier = Modifier.width(130.dp)
                                            )

                                            // Due Date / Status
                                            Column(modifier = Modifier.width(140.dp)) {
                                                Text(
                                                    text = loan.dueDate,
                                                    style = MaterialTheme.typography.bodySmall.copy(
                                                        fontWeight = if (loan.status == LoanStatus.OVERDUE) FontWeight.Bold else FontWeight.Normal,
                                                        color = if (loan.status == LoanStatus.OVERDUE) DangerRed else TextPrimary
                                                    )
                                                )
                                                if (loan.status == LoanStatus.OVERDUE) {
                                                    StatusBadge(
                                                        text = "${loan.daysOverdue}d overdue",
                                                        type = BadgeType.DANGER
                                                    )
                                                } else {
                                                    StatusBadge(text = "Active Loan", type = BadgeType.INFO)
                                                }
                                            }

                                            // One-tap Mark Returned Button
                                            Button(
                                                onClick = { viewModel.returnBook(loan.id) },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = SuccessGreen,
                                                    contentColor = Color.White
                                                ),
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier
                                                    .width(130.dp)
                                                    .testTag("mark_returned_${loan.id}")
                                            ) {
                                                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(14.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Mark Returned", fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
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
}
