package com.example.qrgenerator

import kotlin.js.JsAny

// External declarations for qrcode-generator npm package
@JsModule("qrcode-generator")
external fun qrcode(typeNumber: Int, errorCorrectionLevel: String): QrCodeJs

external interface QrCodeJs : JsAny {
    fun addData(data: String)
    fun make()
    fun getModuleCount(): Int
    fun isDark(row: Int, col: Int): Boolean
}

actual fun generateQrMatrix(data: String): List<List<Boolean>> {
    val qr = qrcode(0, "M") // type 0 = auto-detect size, M = medium error correction
    qr.addData(data)
    qr.make()
    val size = qr.getModuleCount()
    return List(size) { row ->
        List(size) { col ->
            qr.isDark(row, col)
        }
    }
}

@JsFun("""
(title, moduleCount, qrBits, filename) => {
    try {
        // High resolution Canvas (800 x 1720 px, 2x retina mobile scale)
        const canvas = document.createElement('canvas');
        canvas.width = 800;
        canvas.height = 1720;
        const ctx = canvas.getContext('2d');
        if (!ctx) return;

        // Helper: draw rounded rectangle using bezier / arc paths (100% browser compatible)
        function drawRoundedRect(ctx, x, y, width, height, radius, fillColor, strokeColor, strokeWidth) {
            ctx.beginPath();
            ctx.moveTo(x + radius, y);
            ctx.lineTo(x + width - radius, y);
            ctx.quadraticCurveTo(x + width, y, x + width, y + radius);
            ctx.lineTo(x + width, y + height - radius);
            ctx.quadraticCurveTo(x + width, y + height, x + width - radius, y + height);
            ctx.lineTo(x + radius, y + height);
            ctx.quadraticCurveTo(x, y + height, x, y + height - radius);
            ctx.lineTo(x, y + radius);
            ctx.quadraticCurveTo(x, y, x + radius, y);
            ctx.closePath();

            if (fillColor) {
                ctx.fillStyle = fillColor;
                ctx.fill();
            }
            if (strokeColor && strokeWidth) {
                ctx.strokeStyle = strokeColor;
                ctx.lineWidth = strokeWidth;
                ctx.stroke();
            }
        }

        // 1. Phone Body Background & Bezel
        drawRoundedRect(ctx, 16, 16, 768, 1688, 88, '#141414', '#2E2E2E', 3);
        drawRoundedRect(ctx, 36, 36, 728, 1648, 72, '#1A1A1A', null, 0);

        // 2. Status Bar
        ctx.fillStyle = '#FFFFFF';
        ctx.font = '600 30px -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif';
        ctx.textAlign = 'left';
        ctx.fillText('9:41', 88, 110);

        // Dynamic Island
        drawRoundedRect(ctx, 285, 75, 230, 68, 34, '#000000', null, 0);

        // 5G & 100% (Clean pure white text)
        ctx.fillStyle = '#FFFFFF';
        ctx.textAlign = 'right';
        ctx.font = '600 26px -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif';
        ctx.fillText('5G  100%', 712, 110);

        // 3. Navigation Bar
        // Red back chevron (<)
        ctx.strokeStyle = '#FF5252';
        ctx.lineWidth = 6;
        ctx.lineCap = 'round';
        ctx.lineJoin = 'round';
        ctx.beginPath();
        ctx.moveTo(96, 210);
        ctx.lineTo(80, 226);
        ctx.lineTo(96, 242);
        ctx.stroke();

        // Q R   S T U D I O label
        ctx.fillStyle = '#8E8E93';
        ctx.textAlign = 'center';
        ctx.font = '600 22px -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif';
        ctx.letterSpacing = '4px';
        ctx.fillText('Q R   S T U D I O', 400, 232);
        ctx.letterSpacing = '0px';

        // 4. Title Text (wrapped & centered)
        ctx.fillStyle = '#FFFFFF';
        ctx.font = 'bold 30px -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif';
        ctx.textAlign = 'center';
        
        // Wrap title lines
        const words = title.split(' ');
        let lines = [];
        let curLine = '';
        for (let i = 0; i < words.length; i++) {
            let testLine = curLine ? (curLine + ' ' + words[i]) : words[i];
            if (ctx.measureText(testLine).width > 640 && curLine) {
                lines.push(curLine);
                curLine = words[i];
            } else {
                curLine = testLine;
            }
        }
        if (curLine) lines.push(curLine);

        let titleY = 320;
        for (let i = 0; i < lines.length; i++) {
            ctx.fillText(lines[i], 400, titleY);
            titleY += 42;
        }

        // 5. Styled QR Card Frame
        const qrFrameSize = 640;
        const qrFrameX = (800 - qrFrameSize) / 2;
        const qrFrameY = Math.max(titleY + 30, 460);

        // Outer coral-red frame
        drawRoundedRect(ctx, qrFrameX, qrFrameY, qrFrameSize, qrFrameSize, 60, '#FF5252', null, 0);

        // Inner off-white card
        const innerPad = 20;
        const innerSize = qrFrameSize - innerPad * 2;
        drawRoundedRect(ctx, qrFrameX + innerPad, qrFrameY + innerPad, innerSize, innerSize, 48, '#FFF9F5', null, 0);

        // Draw QR Code Modules from qrBits
        if (moduleCount > 0 && qrBits && qrBits.length >= moduleCount * moduleCount) {
            const qrDrawPad = 44;
            const qrDrawSize = innerSize - qrDrawPad * 2;
            const qrDrawX = qrFrameX + innerPad + qrDrawPad;
            const qrDrawY = qrFrameY + innerPad + qrDrawPad;
            const cellSize = qrDrawSize / moduleCount;

            ctx.fillStyle = '#000000';
            for (let r = 0; r < moduleCount; r++) {
                for (let c = 0; c < moduleCount; c++) {
                    const idx = r * moduleCount + c;
                    if (qrBits.charAt(idx) === '1') {
                        ctx.fillRect(
                            qrDrawX + c * cellSize,
                            qrDrawY + r * cellSize,
                            Math.ceil(cellSize),
                            Math.ceil(cellSize)
                        );
                    }
                }
            }
        }

        // 6. Save Button (Coral-Red Pill)
        const btnY = 1480;
        drawRoundedRect(ctx, 80, btnY, 640, 110, 55, '#FF5252', null, 0);
        ctx.fillStyle = '#000000';
        ctx.font = 'bold 34px -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif';
        ctx.textAlign = 'center';
        ctx.fillText('Save', 400, btnY + 68);

        // 7. Home Indicator Bar
        drawRoundedRect(ctx, 270, 1640, 260, 10, 5, 'rgba(255, 255, 255, 0.25)', null, 0);

        // Download canvas as PNG
        const dataUrl = canvas.toDataURL('image/png');
        const a = document.createElement('a');
        a.href = dataUrl;
        a.download = filename;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);

    } catch (e) {
        console.error('Error generating mobile frame snapshot:', e);
    }
}
""")
external fun jsDownloadMobileFrameSnapshot(
    title: String,
    moduleCount: Int,
    qrBits: String,
    filename: String
)

actual fun downloadQrScreenCapture(
    title: String,
    moduleCount: Int,
    qrBits: String,
    filename: String
) {
    jsDownloadMobileFrameSnapshot(title, moduleCount, qrBits, filename)
}
