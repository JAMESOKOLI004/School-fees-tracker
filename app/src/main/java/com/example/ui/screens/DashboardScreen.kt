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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ActivityLog
import com.example.model.ActivityType
import com.example.ui.LibraryViewModel
import com.example.ui.Screen
import com.example.ui.components.StatCard
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.DangerContainer
import com.example.ui.theme.DangerRed
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.Navy100
import com.example.ui.theme.Navy700
import com.example.ui.theme.SuccessContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun DashboardScreen(
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier
) {
    val stats by viewModel.stats.collectAsState()
    val activities by viewModel.activities.collectAsState()
    val librarian by viewModel.librarian.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Top Header with greeting and date
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Dashboard Overview",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 24.sp
                        )
                    )
                    Text(
                        text = "Welcome back, ${librarian.name} • Academic Year 2026-2027",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = { viewModel.openAddBook() },
                        colors = ButtonDefaults.buttonColors(containerColor = Navy700),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("quick_add_book_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Add Book", fontSize = 13.sp)
                    }

                    OutlinedButton(
                        onClick = {
                            viewModel.borrowReturnTab.value = 0
                            viewModel.navigateTo(Screen.BORROW_RETURN)
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("quick_issue_btn")
                    ) {
                        Icon(imageVector = Icons.Default.SwapHoriz, contentDescription = null, modifier = Modifier.size(16.dp), tint = Navy700)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Issue Loan", fontSize = 13.sp, color = Navy700)
                    }
                }
            }
        }

        // 4 KPI Stat Cards
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    StatCard(
                        title = "Total Books",
                        value = "${stats.totalBookTitles}",
                        subtitle = "${stats.totalCopies} copies stocked",
                        icon = Icons.Default.Book,
                        iconColor = Navy700,
                        iconBgColor = Navy100,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.navigateTo(Screen.CATALOG) }
                    )

                    StatCard(
                        title = "Books Borrowed",
                        value = "${stats.totalBorrowedCount}",
                        subtitle = "${stats.totalAvailableCopies} available on shelf",
                        icon = Icons.Default.SwapHoriz,
                        iconColor = Color(0xFF2563EB),
                        iconBgColor = Color(0xFFEFF6FF),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            viewModel.borrowReturnTab.value = 1
                            viewModel.navigateTo(Screen.BORROW_RETURN)
                        }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    StatCard(
                        title = "Overdue Books",
                        value = "${stats.totalOverdueCount}",
                        subtitle = "\$${String.format(Locale.US, "%.2f", stats.totalOverdueFines)} accumulated fines",
                        icon = Icons.Default.Warning,
                        iconColor = DangerRed,
                        iconBgColor = DangerContainer,
                        modifier = Modifier.weight(1f),
                        highlightBorder = if (stats.totalOverdueCount > 0) DangerRed.copy(alpha = 0.5f) else null,
                        onClick = { viewModel.navigateTo(Screen.OVERDUE) }
                    )

                    StatCard(
                        title = "Total Students",
                        value = "${stats.totalStudentsCount}",
                        subtitle = "Active library patrons",
                        icon = Icons.Default.People,
                        iconColor = GoldAccent,
                        iconBgColor = GoldContainer,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.navigateTo(Screen.STUDENTS) }
                    )
                }
            }
        }

        // Collection Status & Circulation Progress Bar
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, BorderSubtle)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Collection Utilization & Circulation",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                fontSize = 15.sp
                            )
                        )

                        val borrowedRatio = if (stats.totalCopies > 0) {
                            (stats.totalBorrowedCount.toFloat() / stats.totalCopies * 100).toInt()
                        } else 0

                        Text(
                            text = "$borrowedRatio% In Circulation",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = GoldAccent,
                                fontSize = 12.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val progressFraction = if (stats.totalCopies > 0) {
                        stats.totalBorrowedCount.toFloat() / stats.totalCopies
                    } else 0f

                    LinearProgressIndicator(
                        progress = { progressFraction.coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = Navy700,
                        trackColor = Color(0xFFE2E8F0)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Navy700))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Borrowed: ${stats.totalBorrowedCount}",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFE2E8F0)))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Available on Shelf: ${stats.totalAvailableCopies}",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(DangerRed))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Overdue: ${stats.totalOverdueCount}",
                                style = MaterialTheme.typography.labelSmall.copy(color = DangerRed, fontWeight = FontWeight.SemiBold)
                            )
                        }
                    }
                }
            }
        }

        // Recent Activity Feed Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Activity Feed",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 16.sp
                    )
                )

                Text(
                    text = "Live Audit Log",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                )
            }
        }

        // Activity Feed List
        items(activities, key = { it.id }) { log ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("activity_item_${log.id}"),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, BorderSubtle)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val (icon, tint, bg) = when (log.type) {
                        ActivityType.BORROW -> Triple(Icons.Default.SwapHoriz, Navy700, Navy100)
                        ActivityType.RETURN -> Triple(Icons.Default.ReceiptLong, SuccessGreen, SuccessContainer)
                        ActivityType.OVERDUE_ALERT -> Triple(Icons.Default.NotificationsActive, DangerRed, DangerContainer)
                        ActivityType.BOOK_ADDED -> Triple(Icons.Default.Book, GoldAccent, GoldContainer)
                        ActivityType.STUDENT_ADDED -> Triple(Icons.Default.PersonAdd, Color(0xFF6366F1), Color(0xFFEEF2FF))
                        ActivityType.FINE_COLLECTED -> Triple(Icons.Default.MonetizationOn, SuccessGreen, SuccessContainer)
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(bg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = log.title,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary,
                                fontSize = 13.sp
                            )
                        )
                        Text(
                            text = log.description,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        )
                    }

                    Text(
                        text = log.timestamp,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }
    }
}
