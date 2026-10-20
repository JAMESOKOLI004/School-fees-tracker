package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.LibraryViewModel
import com.example.ui.components.BadgeType
import com.example.ui.components.StatusBadge
import com.example.ui.components.TableHeader
import com.example.ui.components.TableHeaderCell
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.DangerRed
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.Navy100
import com.example.ui.theme.Navy700
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun StudentsScreen(
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier
) {
    val students by viewModel.filteredStudents.collectAsState()
    val searchQuery by viewModel.studentSearchQuery.collectAsState()
    val showAddDialog by viewModel.showAddStudentDialog.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        // Header & Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Students & Patrons",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 24.sp
                    )
                )
                Text(
                    text = "Manage registered borrowers, active loans, and eligibility",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                )
            }

            Button(
                onClick = { viewModel.showAddStudentDialog.value = true },
                colors = ButtonDefaults.buttonColors(containerColor = Navy700),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("add_student_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Add Student", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.studentSearchQuery.value = it },
            placeholder = { Text("Search by student name, ID (e.g. STU-2026-104), or email...", fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Navy700) },
            trailingIcon = {
                if (searchQuery.isNotBlank()) {
                    IconButton(onClick = { viewModel.studentSearchQuery.value = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear", modifier = Modifier.size(18.dp))
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("student_search_input"),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
        )

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
                Box(modifier = Modifier.horizontalScroll(tableScrollState)) {
                    Column(modifier = Modifier.width(960.dp)) {
                        TableHeader {
                            TableHeaderCell("Student & ID", modifier = Modifier.width(280.dp))
                            TableHeaderCell("Department / Year", modifier = Modifier.width(220.dp))
                            TableHeaderCell("Contact Info", modifier = Modifier.width(220.dp))
                            TableHeaderCell("Active Loans", modifier = Modifier.width(120.dp))
                            TableHeaderCell("Actions", modifier = Modifier.width(120.dp))
                        }

                        if (students.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(240.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No students found matching your search", color = TextMuted)
                            }
                        } else {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                items(students, key = { it.id }) { student ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 12.dp)
                                            .testTag("student_row_${student.id}"),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Student Avatar + Name + Code
                                        Row(
                                            modifier = Modifier.width(280.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(38.dp)
                                                    .clip(CircleShape)
                                                    .background(Navy100),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = student.fullName.take(2).uppercase(),
                                                    style = MaterialTheme.typography.labelMedium.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        color = Navy700
                                                    )
                                                )
                                            }

                                            Spacer(modifier = Modifier.width(12.dp))

                                            Column {
                                                Text(
                                                    text = student.fullName,
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = TextPrimary,
                                                        fontSize = 13.sp
                                                    )
                                                )
                                                Text(
                                                    text = student.studentCode,
                                                    style = MaterialTheme.typography.bodySmall.copy(
                                                        color = TextMuted,
                                                        fontSize = 11.sp
                                                    )
                                                )
                                            }
                                        }

                                        // Department & Year
                                        Column(modifier = Modifier.width(220.dp)) {
                                            Text(
                                                text = student.department,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    color = TextPrimary,
                                                    fontSize = 13.sp
                                                )
                                            )
                                            Text(
                                                text = student.yearLevel,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = TextSecondary,
                                                    fontSize = 11.sp
                                                )
                                            )
                                        }

                                        // Contact Info
                                        Column(modifier = Modifier.width(220.dp)) {
                                            Text(
                                                text = student.email,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = TextSecondary,
                                                    fontSize = 12.sp
                                                )
                                            )
                                            Text(
                                                text = student.phone,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = TextMuted,
                                                    fontSize = 11.sp
                                                )
                                            )
                                        }

                                        // Active Loans Badge
                                        Box(modifier = Modifier.width(120.dp)) {
                                            if (student.activeLoansCount > 0) {
                                                StatusBadge(
                                                    text = "${student.activeLoansCount} Borrowed",
                                                    type = if (student.unpaidFineAmount > 0) BadgeType.WARNING else BadgeType.INFO
                                                )
                                            } else {
                                                StatusBadge(text = "0 Active", type = BadgeType.NEUTRAL)
                                            }
                                        }

                                        // Actions
                                        Row(
                                            modifier = Modifier.width(120.dp),
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            OutlinedButton(
                                                onClick = { viewModel.startBorrowForStudent(student.id) },
                                                shape = RoundedCornerShape(6.dp),
                                                modifier = Modifier.testTag("issue_loan_student_${student.id}")
                                            ) {
                                                Icon(Icons.Default.SwapHoriz, contentDescription = null, modifier = Modifier.size(14.dp), tint = Navy700)
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Issue", fontSize = 11.sp, color = Navy700)
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

    // Add Student Dialog
    if (showAddDialog) {
        var name by remember { mutableStateOf("") }
        var studentCode by remember { mutableStateOf("STU-2026-" + (300..999).random()) }
        var email by remember { mutableStateOf("") }
        var phone by remember { mutableStateOf("+1 (555) ") }
        var dept by remember { mutableStateOf("Computer Science") }
        var year by remember { mutableStateOf("Sophomore (Year 2)") }

        AlertDialog(
            onDismissRequest = { viewModel.showAddStudentDialog.value = false },
            title = {
                Text(
                    text = "Register New Student",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            if (email.isBlank() && it.isNotBlank()) {
                                email = it.lowercase().replace(" ", ".") + "@university.edu"
                            }
                        },
                        label = { Text("Full Name *") },
                        modifier = Modifier.fillMaxWidth().testTag("add_student_name_input"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = studentCode,
                        onValueChange = { studentCode = it },
                        label = { Text("Student Identification Code *") },
                        modifier = Modifier.fillMaxWidth().testTag("add_student_code_input"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("University Email") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = dept,
                        onValueChange = { dept = it },
                        label = { Text("Department / Major") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = year,
                        onValueChange = { year = it },
                        label = { Text("Year Level") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (name.isBlank() || studentCode.isBlank()) {
                            viewModel.showToast("Student name and code are required")
                            return@Button
                        }
                        viewModel.addStudent(
                            name = name,
                            studentCode = studentCode,
                            email = email.ifBlank { "student@university.edu" },
                            phone = phone,
                            dept = dept,
                            year = year
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Navy700),
                    modifier = Modifier.testTag("save_student_confirm_btn")
                ) {
                    Text("Register Student")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { viewModel.showAddStudentDialog.value = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
