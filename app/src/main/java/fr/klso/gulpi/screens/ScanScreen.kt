package fr.klso.gulpi.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

@Composable
fun ScanScreen() {
    val context = LocalContext.current
    if (!canUseCamera(context)) {
        return
    }
}

private fun canUseCamera(ctx: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        ctx, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}

private fun processImage(image: InputImage) {
    val scanner = BarcodeScanning.getClient()

    val result = scanner.process(image)
        .addOnSuccessListener { barcodes ->
            println(barcodes)
        }
        .addOnFailureListener {
            println("failed")
        }
}