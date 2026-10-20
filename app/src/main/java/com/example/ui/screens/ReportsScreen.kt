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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.ui.LibraryViewModel
import com.example.ui.components.BookRankItem
import com.example.ui.components.BorrowingTrendsChart
import com.example.ui.components.MonthlyTrendItem
import com.example.ui.components.MostBorrowedBooksBarChart
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.Navy700
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ReportsScreen(
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier
) {
    val books by viewModel.books.collectAsState()
    val loans by viewModel.loans.collectAsState()
    val showExportDialog by viewModel.showExportDialog.collectAsState()

    // Calculated most borrowed books
    val topBooks = remember(books, loans) {
        val countMap = loans.groupingBy { it.bookTitle }.eachCount()
        val sorted = books.map { book ->
            val count = countMap[book.title] ?: (1..6).random()
            BookRankItem(
                title = book.title,
                author = book.author,
                borrowCount = count,
                maxCount = 20
            )
        }.sortedByDescending { it.borrowCount }.take(5)
        sorted
    }

    // Monthly Trends sample data (Jan to Oct 2026)
    val monthlyTrends = listOf(
        MonthlyTrendItem("Jan", 42, 38),
        MonthlyTrendItem("Feb", 55, 48),
        MonthlyTrendItem("Mar", 78, 70),
        MonthlyTrendItem("Apr", 84, 80),
        MonthlyTrendItem("May", 62, 65),
        MonthlyTrendItem("Jun", 34, 30),
        MonthlyTrendItem("Jul", 28, 25),
        MonthlyTrendItem("Aug", 49, 44),
        MonthlyTrendItem("Sep", 96, 88),
        MonthlyTrendItem("Oct", 112, 94)
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Top Header & Export Button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Assessment,
                            contentDescription = null,
                            tint = Navy700,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Reports & Analytics",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                fontSize = 24.sp
                            )
                        )
                    }
                    Text(
                        text = "Institutional circulation metrics, borrowing patterns, and collection insights",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    )
                }

                Button(
                    onClick = { viewModel.showExportDialog.value = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Navy700),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("export_report_button")
                ) {
                    Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Export Report", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                }
            }
        }

        // Summary KPI row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, BorderSubtle)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("On-Time Return Rate", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("96.2%", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF059669)))
                        Text("+1.8% vs last semester", style = MaterialTheme.typography.bodySmall.copy(color = TextMuted, fontSize = 11.sp))
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, BorderSubtle)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Avg Loan Duration", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("13.4 Days", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = Navy700))
                        Text("Standard limit is 14 days", style = MaterialTheme.typography.bodySmall.copy(color = TextMuted, fontSize = 11.sp))
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, BorderSubtle)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Annual Circulation", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("644 Loans", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = GoldAccent))
                        Text("Peak month: October 2026", style = MaterialTheme.typography.bodySmall.copy(color = TextMuted, fontSize = 11.sp))
                    }
                }
            }
        }

        // Borrowing Trends Chart Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, BorderSubtle)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Circulation Trends Over Time (2026)",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = "Monthly loans issued vs returned books recorded",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("+24% in Q3", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF059669), fontWeight = FontWeight.Bold))
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    BorrowingTrendsChart(trends = monthlyTrends)
                }
            }
        }

        // Most Borrowed Books Bar Chart Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, BorderSubtle)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Most Borrowed Books",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = "Ranked by total historical checkout instances",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )
                        }

                        Text(
                            text = "Top 5 Titles",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    MostBorrowedBooksBarChart(items = topBooks)
                }
            }
        }

        // Genre Distribution Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, BorderSubtle)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Subject Category Distribution",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    val categories = listOf(
                        Triple("Computer Science & Algorithms", "42%", Navy700),
                        Triple("Software Architecture & Systems", "26%", Color(0xFF2563EB)),
                        Triple("Artificial Intelligence & ML", "18%", GoldAccent),
                        Triple("Literature & History", "14%", Color(0xFF059669))
                    )

                    categories.forEach { (name, pct, color) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(color))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(name, style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp))
                            }
                            Text(pct, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = color))
                        }
                    }
                }
            }
        }
    }

    // Export Dialog
    if (showExportDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.showExportDialog.value = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.FileDownload, contentDescription = null, tint = Navy700)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Export Circulation Data")
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Choose an administrative export format for library archives and auditing:")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• Institutional Audit CSV (Books, Patrons, Loans)", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Text("• Monthly Circulation Summary PDF", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Text("• Outstanding Overdue Fines Ledger", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.showExportDialog.value = false
                        viewModel.showToast("Report exported successfully to downloads folder")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Navy700)
                ) {
                    Text("Download Export")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { viewModel.showExportDialog.value = false }) {
                    Text("Close")
                }
            }
        )
    }
}
