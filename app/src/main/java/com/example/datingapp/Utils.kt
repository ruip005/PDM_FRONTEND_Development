package com.example.datingapp

import androidx.appcompat.app.AlertDialog
import android.content.Context
import android.widget.Toast

object DialogUtils {

    /**
     * Exibe um popup de erro com título e mensagem.
     *
     * @param context Contexto da activity ou fragment.
     * @param title O título do popup.
     * @param message A mensagem do popup.
     */
    fun showErrorPopup(context: Context, title: String, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    fun showSuccessPopup(context: RegisterActivity, title: String, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    fun showErrorToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showSuccessToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}