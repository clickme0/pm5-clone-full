package com.pm5clone

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.pm5clone.ble.BleManager
import com.pm5clone.ble.RowingData
import com.pm5clone.ui.Pm5Screen

class MainActivity : ComponentActivity() {
    private lateinit var bleManager: BleManager

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            bleManager.startScan()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bleManager = BleManager(applicationContext)
        requestPermissions()

        setContent {
            val data = remember { mutableStateOf(RowingData()) }
            val status = remember { mutableStateOf("Не подключено") }
            val showSniffer = remember { mutableStateOf(false) }
            val rawLog = remember { mutableStateOf(bleManager.getRawLog()) }

            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Pm5Screen(
                        data = data.value,
                        status = status.value,
                        rawLog = rawLog.value,
                        showSniffer = showSniffer.value,
                        onToggleSniffer = { showSniffer.value = !showSniffer.value },
                        onScan = {
                            status.value = "Сканирование..."
                            bleManager.startScan()
                        },
                        onDisconnect = {
                            bleManager.disconnect()
                            status.value = "Отключено"
                        }
                    )
                }
            }
        }
    }

    private fun requestPermissions() {
        val permissions = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
        permissionLauncher.launch(permissions)
    }
}
