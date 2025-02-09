package com.example.datingapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import com.google.zxing.integration.android.IntentIntegrator
import java.util.regex.Pattern

class scanner_block(private val activity: AppCompatActivity, private val scanIcon: ImageView) {

    private lateinit var qrCodeLauncher: ActivityResultLauncher<Intent>

    init {
        // Configura o scanner QR Code ao iniciar
        qrCodeLauncher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data = result.data
            val intentResult = IntentIntegrator.parseActivityResult(result.resultCode, data)
            if (intentResult != null && intentResult.contents != null) {
                val scannedData = intentResult.contents
                if (isValidUrl(scannedData)) {
                    openUrl(activity, scannedData)
                }
            }
        }

        // Definir o clique no ícone para iniciar o scanner
        scanIcon.setOnClickListener {
            iniciarLeitorQRCode()
        }
    }

    private fun iniciarLeitorQRCode() {
        val integrator = IntentIntegrator(activity)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Aponte para o código QR")
        integrator.setCameraId(0)
        integrator.setBeepEnabled(true)
        integrator.setBarcodeImageEnabled(true)
        qrCodeLauncher.launch(integrator.createScanIntent())
    }

    private fun openUrl(context: Context, url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(browserIntent)
    }

    private fun isValidUrl(url: String): Boolean {
        val urlPattern = Pattern.compile("^(http|https)://.*$", Pattern.CASE_INSENSITIVE)
        return urlPattern.matcher(url).matches()
    }
}
