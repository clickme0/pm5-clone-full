package com.pm5clone.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log

class BleManager(private val context: Context) {
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private val rawLog = mutableListOf<RawPacket>()  // ← ДОБАВИТЬ ЭТУ СТРОКУ
    
    fun startScan() {
        Log.d("BleManager", "Start scan")
        // Здесь будет логика сканирования BLE
    }
    
    fun connectToDevice(device: BluetoothDevice) {
        Log.d("BleManager", "Connect to ${device.address}")
        // Здесь будет логика подключения
    }
    
    // ↓↓↓ ЭТИ 2 МЕТОДА ДОБАВИТЬ ↓↓↓
    
    fun disconnect() {
        Log.d("BleManager", "Disconnect")
        rawLog.clear()
    }
    
    fun getRawLog(): List<RawPacket> = rawLog
    
    // ↑↑↑ ЭТИ 2 МЕТОДА ДОБАВИТЬ ↑↑↑
}
