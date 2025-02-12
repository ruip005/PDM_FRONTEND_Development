package com.example.datingapp.Activities

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.datingapp.adapters.BluetoothDevicesAdapter
import com.example.datingapp.R

class BluetoothDevicesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BluetoothDevicesAdapter
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_devices)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "O dispositivo não suporta Bluetooth", Toast.LENGTH_SHORT).show()
            finish()
        }

        verificarPermissoesBluetooth()
    }

    private fun verificarPermissoesBluetooth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 1)
                return
            }
        }
        listarDispositivos()
    }

    private fun listarDispositivos() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 1)
                return
            }
        }

        // Agora é seguro acessar os dispositivos emparelhados
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        val listaDispositivos = pairedDevices?.map { it.name ?: "Desconhecido" } ?: listOf("Nenhum dispositivo emparelhado")

        adapter = BluetoothDevicesAdapter(listaDispositivos)
        recyclerView.adapter = adapter
    }

    private fun TurnOnIfTurnOff(){
        // Ligar o bluettoth se tiver off

    }

}
