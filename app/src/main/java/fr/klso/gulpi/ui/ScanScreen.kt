package fr.klso.gulpi.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import fr.klso.gulpi.navigation.Search

@Composable
@androidx.camera.core.ExperimentalGetImage
fun ScanScreen(navController: NavController) {
    val context = LocalContext.current
    if (!canUseCamera(context)) {
        println("cannot use camera")
        return
    }

    val lensFacing = CameraSelector.LENS_FACING_BACK
    val lifecycleOwner = LocalLifecycleOwner.current

    val preview = Preview.Builder().build()
    val previewView = remember { PreviewView(context) }
    val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()

    println("start camera")
    val cameraController = LifecycleCameraController(context)
    val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
        .build()
    val barcodeScanner = BarcodeScanning.getClient(options)

    println("setImageAnalysisAnalyzer")
    cameraController.setImageAnalysisAnalyzer(
        ContextCompat.getMainExecutor(context),
        BarcodeImageAnalyzer(
            barcodeScanner,
        ) { barcodes: List<Barcode> ->
            if (barcodes.isNotEmpty()) {
                for (barcode in barcodes) {
                    val barcodeStr = barcode.rawValue
                    if (isBarcodeValid(barcodeStr)) {
                        println(barcodeStr)
                        navController.navigate("${Search.route}/$barcodeStr") {
                            launchSingleTop = true
                        }
                    }
                }
            }
        }
    )

    cameraController.bindToLifecycle(lifecycleOwner)
    previewView.controller = cameraController
    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
        AndroidView({ previewView }, modifier = Modifier.fillMaxSize())
    }
}

private fun isBarcodeValid(barcode: String?): Boolean {
    return barcode != null && barcode.length in 4..23
}

private fun canUseCamera(ctx: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        ctx, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}

@androidx.camera.core.ExperimentalGetImage
class BarcodeImageAnalyzer(
    private val scanner: BarcodeScanner,
    val onBarcodes: (List<Barcode>) -> Unit
) :
    ImageAnalysis.Analyzer {
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    onBarcodes(barcodes)
                }.addOnFailureListener {
                    println(it)
                }.addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }
}