package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.Navy600
import com.example.ui.theme.Navy700
import com.example.ui.theme.SurfaceMuted
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

data class BookRankItem(
    val title: String,
    val author: String,
    val borrowCount: Int,
    val maxCount: Int
)

@Composable
fun MostBorrowedBooksBarChart(
    items: List<BookRankItem>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items.forEachIndexed { index, item ->
            val fraction = if (item.maxCount > 0) item.borrowCount.toFloat() / item.maxCount else 0f
            val animatedFraction by animateFloatAsState(
                targetValue = fraction,
                animationSpec = tween(durationMillis = 600),
                label = "bar_anim"
            )

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(if (index == 0) GoldAccent else Navy700),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${index + 1}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 11.sp
                                )
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary,
                                fontSize = 13.sp
                            ),
                            maxLines = 1
                        )
                    }

                    Text(
                        text = "${item.borrowCount} loans",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (index == 0) GoldAccent else Navy600,
                            fontSize = 12.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Progress Bar Track
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(SurfaceMuted)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(animatedFraction)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (index == 0) GoldAccent else Navy700)
                    )
                }
            }
        }
    }
}

data class MonthlyTrendItem(
    val month: String,
    val loansIssued: Int,
    val booksReturned: Int
)

@Composable
fun BorrowingTrendsChart(
    trends: List<MonthlyTrendItem>,
    modifier: Modifier = Modifier
) {
    val maxVal = trends.maxOfOrNull { maxOf(it.loansIssued, it.booksReturned) } ?: 100
    val gridMax = ((maxVal + 9) / 10) * 10

    Column(modifier = modifier.fillMaxWidth()) {
        // Legend
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Navy700)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Loans Issued",
                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 11.sp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(GoldAccent)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Returns Recorded",
                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 11.sp)
            )
        }

        // Canvas Bar Chart
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            val chartWidth = size.width
            val chartHeight = size.height - 30f // Leave space for labels
            val groupWidth = chartWidth / trends.size.coerceAtLeast(1)
            val barWidth = (groupWidth * 0.28f).coerceAtLeast(8f)

            // Draw horizontal subtle grid lines
            val lineCount = 4
            for (i in 0..lineCount) {
                val y = chartHeight - (chartHeight / lineCount) * i
                drawLine(
                    color = Color(0xFFE2E8F0),
                    start = Offset(0f, y),
                    end = Offset(chartWidth, y),
                    strokeWidth = 1f
                )
            }

            // Draw bars
            trends.forEachIndexed { index, item ->
                val groupCenterX = groupWidth * index + groupWidth / 2f

                val loanHeight = (item.loansIssued.toFloat() / gridMax) * chartHeight
                val returnHeight = (item.booksReturned.toFloat() / gridMax) * chartHeight

                // Loan Bar (Navy)
                val loanX = groupCenterX - barWidth - 2f
                val loanY = chartHeight - loanHeight
                drawRoundRect(
                    color = Color(0xFF1E3A8A),
                    topLeft = Offset(loanX, loanY),
                    size = Size(barWidth, loanHeight),
                    cornerRadius = CornerRadius(4f, 4f)
                )

                // Return Bar (Gold)
                val returnX = groupCenterX + 2f
                val returnY = chartHeight - returnHeight
                drawRoundRect(
                    color = Color(0xFFD97706),
                    topLeft = Offset(returnX, returnY),
                    size = Size(barWidth, returnHeight),
                    cornerRadius = CornerRadius(4f, 4f)
                )
            }
        }

        // Month Labels below chart
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            trends.forEach { item ->
                Text(
                    text = item.month,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextMuted,
                        fontSize = 10.sp
                    ),
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}
