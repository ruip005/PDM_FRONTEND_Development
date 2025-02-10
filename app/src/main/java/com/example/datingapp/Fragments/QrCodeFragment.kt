package com.example.datingapp.Fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator
import com.example.datingapp.R

class QrScannerFragment : Fragment() {

    private lateinit var btnScanQrCode: Button
    private lateinit var tvScanResult: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar o layout do fragmento
        val view = inflater.inflate(R.layout.fragment_qr_scanner, container, false)

        // Inicializar views
        btnScanQrCode = view.findViewById(R.id.btnScanQrCode)
        //tvScanResult = view.findViewById(R.id.tvScanResult)

        // Configurar o botão para iniciar o scanner
        btnScanQrCode.setOnClickListener {
            iniciarLeitorQRCode()
        }

        return view
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
        // Processar o resultado do scan
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                // Caso o scan seja cancelado
                Toast.makeText(requireContext(), "Scan cancelado", Toast.LENGTH_SHORT).show()
            } else {
                // Exibir o resultado do scan
                //tvScanResult.text = result.contents
                //Toast.makeText(requireContext(), "Resultado: ${result.contents}", Toast.LENGTH_LONG).show()
                //Copiar para o clipboard
                val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("qr_code", result.contents)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(requireContext(), "Resultado copiado para o clipboard", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}