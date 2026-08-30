package com.example.qrgenerator

/**
 * Generates a QR code matrix for the given data.
 * Returns a 2D list where true = dark module, false = light module.
 */
expect fun generateQrMatrix(data: String): List<List<Boolean>>

/**
 * Generates and downloads a pixel-perfect mobile-frame snapshot of the QR screen as PNG.
 * Passes the computed QR matrix bit string so drawing is 100% guaranteed.
 */
expect fun downloadQrScreenCapture(
    title: String,
    moduleCount: Int,
    qrBits: String,
    filename: String
)
