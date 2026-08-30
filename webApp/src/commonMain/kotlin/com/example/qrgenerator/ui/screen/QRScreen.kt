package com.example.qrgenerator.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qrgenerator.downloadQrScreenCapture
import com.example.qrgenerator.generateQrMatrix

private val BgColor = Color(0xFF141414)
private val LabelColor = Color(0xFF8E8E93)
private val AccentRed = Color(0xFFFF5252)
private val QrCardBg = Color(0xFFFFF9F5)

/**
 * Sanitizes title into a clean filename: lowercase, non-alphanumeric replaced with underscore.
 * E.g. "Live Echo Mic : Voice Effects" -> "live_echo_mic_voice_effects.png"
 */
private fun sanitizeTitleToFilename(title: String): String {
    val clean = title
        .lowercase()
        .map { ch -> if (ch in 'a'..'z' || ch in '0'..'9') ch else '_' }
        .joinToString("")
        .replace(Regex("_+"), "_")
        .trim('_')
    
    val base = if (clean.isNotBlank()) clean else "qr_studio"
    return "$base.png"
}

@Composable
fun QrScreen(
    title: String,
    qrData: String,
    onBack: () -> Unit
) {
    val matrix = remember(qrData) { generateQrMatrix(qrData) }
    val moduleCount = matrix.size

    // Build flattened bits string (1 = dark, 0 = light) for export
    val qrBits = remember(matrix) {
        val sb = StringBuilder(moduleCount * moduleCount)
        for (r in 0 until moduleCount) {
            for (c in 0 until moduleCount) {
                sb.append(if (matrix[r][c]) '1' else '0')
            }
        }
        sb.toString()
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
    ) {
        val contentWidth = when {
            maxWidth > 900.dp -> 440.dp
            maxWidth > 600.dp -> maxWidth * 0.72f
            else -> maxWidth
        }
        val hPad = if (maxWidth > 600.dp) 0.dp else 24.dp

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .width(contentWidth)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = hPad)
                    .padding(top = 16.dp, bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // ── Top Navigation Bar ──────────────────────────────
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Vector drawn back chevron (100% reliable, no font glyph issues)
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .size(44.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = onBack
                                ),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Canvas(
                                modifier = Modifier
                                    .size(width = 12.dp, height = 20.dp)
                            ) {
                                val strokeW = 3.dp.toPx()
                                val path = Path().apply {
                                    moveTo(size.width * 0.85f, 0f)
                                    lineTo(size.width * 0.15f, size.height * 0.5f)
                                    lineTo(size.width * 0.85f, size.height)
                                }
                                drawPath(
                                    path = path,
                                    color = AccentRed,
                                    style = Stroke(
                                        width = strokeW,
                                        cap = StrokeCap.Round,
                                        join = StrokeJoin.Round
                                    )
                                )
                            }
                        }

                        // Centered "QR STUDIO" spaced text
                        Text(
                            text = "Q R   S T U D I O",
                            color = LabelColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 2.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // ── Title Text ──────────────────────────────────────
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // ── QR Code with Red Outer Rounded Frame ────────────
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(32.dp))
                            .background(AccentRed)
                            .padding(10.dp), // Red frame border thickness
                        contentAlignment = Alignment.Center
                    ) {
                        // Inner off-white card
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(24.dp))
                                .background(QrCardBg)
                                .padding(18.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .aspectRatio(1f)
                            ) {
                                if (moduleCount > 0) {
                                    val cellSize = size.width / moduleCount
                                    for (row in 0 until moduleCount) {
                                        for (col in 0 until moduleCount) {
                                            if (matrix[row][col]) {
                                                drawRect(
                                                    color = Color.Black,
                                                    topLeft = Offset(
                                                        x = col * cellSize,
                                                        y = row * cellSize
                                                    ),
                                                    size = Size(cellSize, cellSize)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(36.dp))

                // ── Bottom Save Button ───────────────────────────────────
                Button(
                    onClick = {
                        val filename = sanitizeTitleToFilename(title)
                        downloadQrScreenCapture(title, moduleCount, qrBits, filename)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed)
                ) {
                    Text(
                        text = "Save",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        letterSpacing = 0.2.sp
                    )
                }
            }
        }
    }
}
