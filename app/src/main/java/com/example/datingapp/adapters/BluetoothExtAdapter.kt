package com.example.datingapp.adapters

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.datingapp.R

class BluetoothExtAdapter(
    private val context: Context,
    private val deviceList: List<BluetoothDevice>,
    private val onDeviceClickListener: (BluetoothDevice) -> Unit
) : RecyclerView.Adapter<BluetoothExtAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deviceName: TextView = itemView.findViewById(R.id.deviceName)
        val devicePhoneNumber: TextView = itemView.findViewById(R.id.devicePhoneNumber)

        init {
            itemView.setOnClickListener {
                onDeviceClickListener(deviceList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_bluetooth_device, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val device = deviceList[position]
        holder.deviceName.text = device.name ?: "Dispositivo Desconhecido"

        if (checkContactsPermission()) {
            holder.devicePhoneNumber.text = getPhoneNumberFromContact(device.name) ?: "Número não disponível"
        } else {
            holder.devicePhoneNumber.text = "Permissão de contatos negada"
            requestContactsPermission()
        }
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    private fun checkContactsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestContactsPermission() {
        ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CONTACTS_PERMISSION)
    }

    private fun getPhoneNumberFromContact(deviceName: String?): String? {
        if (deviceName == null) return null

        val uri = android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER)
        val selection = "${android.provider.ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} = ?"
        val selectionArgs = arrayOf(deviceName)

        return context.contentResolver.query(uri, projection, selection, selectionArgs, null)?.use { cursor ->
            val columnIndex = cursor.getColumnIndex(android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER)
            if (columnIndex != -1 && cursor.moveToFirst()) {
                cursor.getString(columnIndex)
            } else {
                null
            }
        }
    }

    companion object {
        private const val REQUEST_CONTACTS_PERMISSION = 100

    }
}