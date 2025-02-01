import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import java.io.File
import java.io.FileOutputStream

object QRCodeGenerator {

    fun generateAndSaveQRCode(context: Context, data: String, fileName: String) {
        val writer = QRCodeWriter()
        try {
            val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt())
                }
            }

            // Salvar a imagem no armazenamento interno
            saveImage(context, bmp, fileName)

        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }

    private fun saveImage(context: Context, bitmap: Bitmap, fileName: String) {
        var out: FileOutputStream? = null
        try {
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName)
            out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            Toast.makeText(context, "QR Code salvo em ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            out?.close()
        }
    }

    fun hasQRCode(context: Context, fileName: String): Boolean {
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName)
        return file.exists()
    }
}

/*
// Exemplo de uso
        val userData = "https://example.com/user/12345" // Dados do usuário
        val fileName = "user_qrcode.png" // Nome do arquivo
        QRCodeGenerator.generateAndSaveQRCode(this, userData, fileName)
 */