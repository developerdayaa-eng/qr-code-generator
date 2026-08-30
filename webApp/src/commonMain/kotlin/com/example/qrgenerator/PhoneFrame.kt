package com.example.qrgenerator

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BezelColor  = Color(0xFF141414)
private val BorderColor = Color(0xFF2E2E2E)
private val ButtonColor = Color(0xFF252525)
private val ScreenBg    = Color(0xFF1A1A1A)

@Composable
fun PhoneFrame(
    phoneWidth: Dp = 400.dp,
    phoneHeight: Dp = 860.dp,
    content: @Composable () -> Unit
) {
    val outerRadius = 54.dp
    val innerRadius = 44.dp
    val bezelPad    = 11.dp

    Box(
        modifier = Modifier
            .width(phoneWidth)
            .height(phoneHeight)
            .clip(RoundedCornerShape(outerRadius))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF212121), Color(0xFF141414))
                )
            )
            .border(
                width = 1.5.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF3E3E3E), Color(0xFF1E1E1E))
                ),
                shape = RoundedCornerShape(outerRadius)
            )
    ) {
        // ── Side Buttons ──────────────────────────────────────────────

        // Power button (right)
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = 0.dp, y = 30.dp)
                .size(width = 4.dp, height = 80.dp)
                .clip(RoundedCornerShape(topStart = 3.dp, bottomStart = 3.dp))
                .background(ButtonColor)
        )
        // Volume Up (left)
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(y = 160.dp)
                .size(width = 4.dp, height = 48.dp)
                .clip(RoundedCornerShape(topEnd = 3.dp, bottomEnd = 3.dp))
                .background(ButtonColor)
        )
        // Volume Down (left)
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(y = 220.dp)
                .size(width = 4.dp, height = 48.dp)
                .clip(RoundedCornerShape(topEnd = 3.dp, bottomEnd = 3.dp))
                .background(ButtonColor)
        )
        // Silent switch (left)
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(y = 110.dp)
                .size(width = 4.dp, height = 32.dp)
                .clip(RoundedCornerShape(topEnd = 3.dp, bottomEnd = 3.dp))
                .background(ButtonColor)
        )

        // ── Screen ────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .padding(bezelPad)
                .fillMaxSize()
                .clip(RoundedCornerShape(innerRadius))
                .background(ScreenBg)
        ) {
            // Status Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(horizontal = 28.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Time
                Text(
                    text = "9:41",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                // Dynamic Island pill
                Box(
                    modifier = Modifier
                        .width(116.dp)
                        .height(34.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color.Black)
                )
                // Signal + Battery text (pure clean white, no unicode tofu boxes)
                Text(
                    text = "5G  100%",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // App Content
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                content()
            }

            // Home Indicator
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(136.dp)
                        .height(5.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color.White.copy(alpha = 0.22f))
                )
            }
        }
    }
}

/** Outer shell shown on desktop: dark gradient bg + centered phone frame */
@Composable
fun PhoneShell(content: @Composable () -> Unit) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF1C1C2E), Color(0xFF0A0A0A))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Scale phone to fit viewport with some margin
        val maxH   = maxHeight - 48.dp
        val maxW   = maxWidth  - 48.dp
        val aspect = 400f / 860f
        val pH     = maxH.coerceAtMost(860.dp)
        val pW     = (pH * aspect).coerceAtMost(maxW).coerceAtMost(400.dp)
        val pHFinal = pW / aspect

        PhoneFrame(phoneWidth = pW, phoneHeight = pHFinal) {
            content()
        }
    }
}
