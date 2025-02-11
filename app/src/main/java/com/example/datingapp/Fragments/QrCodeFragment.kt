package com.example.datingapp.Fragments

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator
import com.example.datingapp.R

class QrScannerFragment : Fragment() {

    private lateinit var btnScanQrCode: ImageView // Altere para ImageView
    private lateinit var tvScanResult: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar o layout do fragmento
        val view = inflater.inflate(R.layout.fragment_qr_scanner, container, false)

        // Inicializar views
        btnScanQrCode = view.findViewById(R.id.btnScanQrCode) // Agora é um ImageView
        tvScanResult = view.findViewById(R.id.tvScanResult)

        // Configurar o ImageView para iniciar o scanner
        btnScanQrCode.setOnClickListener {
            checkCameraPermission()
        }

        return view
    }

    private val CAMERA_PERMISSION_REQUEST_CODE = 100

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Solicitar permissão
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permissão já concedida, iniciar o scanner
            iniciarLeitorQRCode()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão concedida, iniciar o scanner
                iniciarLeitorQRCode()
            } else {
                // Permissão negada, exibir mensagem ao usuário
                Toast.makeText(requireContext(), "Permissão da câmera negada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun iniciarLeitorQRCode() {
        // Configurar o scanner
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE) // Definir o formato do código (QR Code)
        integrator.setPrompt("Aponte para o código QR") // Mensagem exibida ao usuário
        integrator.setCameraId(0) // Usar a câmera traseira
        integrator.setBeepEnabled(true) // Ativar som ao escanear
        integrator.setBarcodeImageEnabled(true) // Permitir captura de imagem
        integrator.initiateScan() // Iniciar o scanner
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Processar o resultado do scan
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                // Caso o scan seja cancelado
                Toast.makeText(requireContext(), "Scan cancelado", Toast.LENGTH_SHORT).show()
            } else {
                // Exibir o resultado do scan
                tvScanResult.text = result.contents
                tvScanResult.visibility = View.VISIBLE
                Toast.makeText(requireContext(), "Resultado: ${result.contents}", Toast.LENGTH_LONG).show()
                // Copiar para o clipboard
                val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("qr_code", result.contents)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(requireContext(), "Resultado copiado para o clipboard", Toast.LENGTH_SHORT).show()
            }
        }
    }
}