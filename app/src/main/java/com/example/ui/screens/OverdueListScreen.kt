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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.ui.theme.DangerContainer
import com.example.ui.theme.DangerRed
import com.example.ui.theme.DangerText
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun OverdueListScreen(
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier
) {
    val overdueLoans by viewModel.overdueLoans.collectAsState()
    val totalFines = overdueLoans.sumOf { it.fineAmount }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = DangerRed,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Overdue Loans & Fines",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 24.sp
                        )
                    )
                }
                Text(
                    text = "Items exceeding institutional loan period requiring librarian intervention",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // High-priority Alert Summary Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DangerContainer),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, DangerRed.copy(alpha = 0.4f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${overdueLoans.size} Titles Overdue",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DangerText,
                            fontSize = 16.sp
                        )
                    )
                    Text(
                        text = "Calculated automatically at \$0.50 per day late fee",
                        style = MaterialTheme.typography.bodySmall.copy(color = DangerText.copy(alpha = 0.8f))
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Total Outstanding Fines",
                            style = MaterialTheme.typography.labelSmall.copy(color = DangerText)
                        )
                        Text(
                            text = "\$${String.format(Locale.US, "%.2f", totalFines)}",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DangerRed,
                                fontSize = 24.sp
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Overdue Data Table Card
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
                    Column(modifier = Modifier.width(1040.dp)) {
                        TableHeader {
                            TableHeaderCell("Borrower Student", modifier = Modifier.width(240.dp))
                            TableHeaderCell("Overdue Title", modifier = Modifier.width(280.dp))
                            TableHeaderCell("Due Date", modifier = Modifier.width(110.dp))
                            TableHeaderCell("Days Overdue", modifier = Modifier.width(130.dp))
                            TableHeaderCell("Fine Amount", modifier = Modifier.width(110.dp))
                            TableHeaderCell("Actions", modifier = Modifier.width(170.dp))
                        }

                        if (overdueLoans.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(240.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Great news! There are currently no overdue loans.", color = TextMuted)
                            }
                        } else {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                items(overdueLoans, key = { it.id }) { loan ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(DangerContainer.copy(alpha = 0.15f))
                                            .padding(horizontal = 16.dp, vertical = 12.dp)
                                            .testTag("overdue_row_${loan.id}"),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Borrower
                                        Column(modifier = Modifier.width(240.dp)) {
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

                                        // Title
                                        Column(modifier = Modifier.width(280.dp)) {
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

                                        // Due Date
                                        Text(
                                            text = loan.dueDate,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = DangerRed,
                                                fontWeight = FontWeight.SemiBold
                                            ),
                                            modifier = Modifier.width(110.dp)
                                        )

                                        // Days Overdue
                                        Box(modifier = Modifier.width(130.dp)) {
                                            Row(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(DangerContainer)
                                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Warning,
                                                    contentDescription = null,
                                                    tint = DangerRed,
                                                    modifier = Modifier.size(12.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "${loan.daysOverdue} days late",
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        color = DangerRed,
                                                        fontSize = 11.sp
                                                    )
                                                )
                                            }
                                        }

                                        // Fine Amount
                                        Text(
                                            text = "\$${String.format(Locale.US, "%.2f", loan.fineAmount)}",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = DangerRed,
                                                fontSize = 14.sp
                                            ),
                                            modifier = Modifier.width(110.dp)
                                        )

                                        // Actions
                                        Row(
                                            modifier = Modifier.width(170.dp),
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Button(
                                                onClick = { viewModel.returnBook(loan.id) },
                                                colors = ButtonDefaults.buttonColors(containerColor = DangerRed),
                                                shape = RoundedCornerShape(6.dp),
                                                modifier = Modifier.testTag("settle_return_${loan.id}")
                                            ) {
                                                Text("Settle & Return", fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                                            }

                                            OutlinedButton(
                                                onClick = { viewModel.sendOverdueReminder(loan.id) },
                                                shape = RoundedCornerShape(6.dp),
                                                modifier = Modifier.testTag("remind_${loan.id}")
                                            ) {
                                                Icon(Icons.Default.NotificationsActive, contentDescription = null, modifier = Modifier.size(12.dp), tint = GoldAccent)
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
