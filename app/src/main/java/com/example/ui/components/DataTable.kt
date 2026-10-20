package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.DangerContainer
import com.example.ui.theme.DangerRed
import com.example.ui.theme.DangerText
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldDark
import com.example.ui.theme.Navy100
import com.example.ui.theme.Navy700
import com.example.ui.theme.SuccessContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessText
import com.example.ui.theme.SurfaceMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun TableHeader(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
            .background(SurfaceMuted)
            .border(1.dp, BorderSubtle, RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

@Composable
fun TableHeaderCell(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            color = TextSecondary,
            fontSize = 11.sp,
            letterSpacing = 0.5.sp
        ),
        modifier = modifier
    )
}

@Composable
fun StatusBadge(
    text: String,
    type: BadgeType,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, dotColor) = when (type) {
        BadgeType.SUCCESS -> Triple(SuccessContainer, SuccessText, SuccessGreen)
        BadgeType.WARNING -> Triple(GoldContainer, GoldDark, GoldAccent)
        BadgeType.DANGER -> Triple(DangerContainer, DangerText, DangerRed)
        BadgeType.INFO -> Triple(Navy100, Navy700, Color(0xFF2563EB))
        BadgeType.NEUTRAL -> Triple(Color(0xFFF1F5F9), Color(0xFF475569), Color(0xFF94A3B8))
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(dotColor)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = textColor,
                fontSize = 11.sp
            )
        )
    }
}

enum class BadgeType {
    SUCCESS,
    WARNING,
    DANGER,
    INFO,
    NEUTRAL
}

@Composable
fun BookCoverThumbnail(
    title: String,
    gradientIndex: Int,
    modifier: Modifier = Modifier
) {
    val gradients = listOf(
        listOf(Color(0xFF1E3A8A), Color(0xFF3B82F6)),
        listOf(Color(0xFF0F766E), Color(0xFF14B8A6)),
        listOf(Color(0xFFB45309), Color(0xFFF59E0B)),
        listOf(Color(0xFF6D28D9), Color(0xFF8B5CF6)),
        listOf(Color(0xFF831843), Color(0xFFEC4899))
    )
    val colors = gradients[gradientIndex % gradients.size]

    Box(
        modifier = modifier
            .size(width = 36.dp, height = 48.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Brush.verticalGradient(colors)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title.take(2).uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 12.sp
            )
        )
    }
}
