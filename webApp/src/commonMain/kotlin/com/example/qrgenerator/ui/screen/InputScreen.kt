package com.example.qrgenerator.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Design tokens — matching the shared screenshot
private val BgColor      = Color(0xFF1A1A1A)
private val CardColor    = Color(0xFF272727)
private val LabelColor   = Color(0xFF8E8E93)
private val AccentRed    = Color(0xFFFF5252)
private val ErrorColor   = Color(0xFFFF5252)

@Composable
fun InputScreen(
    title: String,
    qrData: String,
    onTitleChange: (String) -> Unit,
    onQrDataChange: (String) -> Unit,
    onNavigate: () -> Unit
) {
    var titleError by remember { mutableStateOf(false) }
    var qrDataError by remember { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
    ) {
        // Responsive content width
        val contentWidth = when {
            maxWidth > 900.dp -> 520.dp
            maxWidth > 600.dp -> maxWidth * 0.72f
            else              -> maxWidth
        }
        val hPad = if (maxWidth > 600.dp) 0.dp else 24.dp

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {

            Column(
                modifier = Modifier
                    .width(contentWidth)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = hPad)
                    .padding(top = 20.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // ── Header ──────────────────────────────────────────────
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "QR Studio",
                            color = Color.White,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.5).sp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(AccentRed)
                        )
                    }

                    Spacer(modifier = Modifier.height(36.dp))

                    // ── TITLE label ─────────────────────────────────────────
                    Text(
                        text = "TITLE",
                        color = if (titleError) ErrorColor else LabelColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.8.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            onTitleChange(it)
                            if (titleError && it.isNotBlank()) {
                                titleError = false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 88.dp),
                        shape = RoundedCornerShape(14.dp),
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 16.sp,
                            lineHeight = 24.sp
                        ),
                        placeholder = {
                            Text(
                                text = "Enter Title here...",
                                color = LabelColor.copy(alpha = 0.5f),
                                fontSize = 16.sp
                            )
                        },
                        isError = titleError,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor   = CardColor,
                            unfocusedContainerColor = CardColor,
                            errorContainerColor     = CardColor,
                            focusedBorderColor      = Color.Transparent,
                            unfocusedBorderColor    = Color.Transparent,
                            errorBorderColor        = ErrorColor,
                            cursorColor             = AccentRed,
                            focusedTextColor        = Color.White,
                            unfocusedTextColor      = Color.White
                        ),
                        maxLines = 4
                    )

                    AnimatedVisibility(
                        visible = titleError,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Text(
                            text = "Please enter a title",
                            color = ErrorColor,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // ── QR DATA label ───────────────────────────────────────
                    Text(
                        text = "QR DATA",
                        color = if (qrDataError) ErrorColor else LabelColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.8.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = qrData,
                        onValueChange = {
                            onQrDataChange(it)
                            if (qrDataError && it.isNotBlank()) {
                                qrDataError = false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 110.dp),
                        shape = RoundedCornerShape(14.dp),
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 16.sp,
                            lineHeight = 24.sp
                        ),
                        placeholder = {
                            Text(
                                text = "Enter QR data",
                                color = LabelColor.copy(alpha = 0.5f),
                                fontSize = 16.sp
                            )
                        },
                        isError = qrDataError,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor   = CardColor,
                            unfocusedContainerColor = CardColor,
                            errorContainerColor     = CardColor,
                            focusedBorderColor      = Color.Transparent,
                            unfocusedBorderColor    = Color.Transparent,
                            errorBorderColor        = ErrorColor,
                            cursorColor             = AccentRed,
                            focusedTextColor        = Color.White,
                            unfocusedTextColor      = Color.White
                        ),
                        maxLines = 6
                    )

                    AnimatedVisibility(
                        visible = qrDataError,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Text(
                            text = "Please enter QR data",
                            color = ErrorColor,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // ── Generate QR Button ───────────────────────────────────
                    Button(
                        onClick = {
                            val cleanTitle = title.trim()
                            val cleanQr = qrData.trim()

                            val isTitleEmpty = cleanTitle.isEmpty()
                            val isQrEmpty = cleanQr.isEmpty()

                            titleError = isTitleEmpty
                            qrDataError = isQrEmpty

                            if (!isTitleEmpty && !isQrEmpty) {
                                onNavigate()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentRed)
                    ) {
                        Text(
                            text = "Generate QR",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            letterSpacing = 0.2.sp
                        )
                    }
                }

                // ── Footer anchored at bottom ───────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, bottom = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "By Developer-Daya",
                        color = Color.White.copy(alpha = 0.22f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraLight,
                        fontStyle = FontStyle.Italic,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}
