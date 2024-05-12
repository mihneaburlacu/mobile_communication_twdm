package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class WifiActivity : AppCompatActivity() {

    private lateinit var webSocketClient: WebSocketClientApp
    private lateinit var wifiManager: WifiManager
    private lateinit var listView: ListView
    private lateinit var infoText: TextView
    private lateinit var textMessages: EditText
    private lateinit var sendButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_activity)

        listView = findViewById(R.id.list_view)
        infoText = findViewById(R.id.info_text)
        textMessages = findViewById(R.id.text_message)
        sendButton = findViewById(R.id.send_button)

        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        webSocketClient = WebSocketClientApp()

        setOnWifiDeviceClickListener()
        setOnSendButtonListener()

        // Check and request necessary permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CHANGE_WIFI_STATE), PERMISSIONS_REQUEST_CODE)
        } else {
            scanWifi()
            giveWifiInterfaceDetails()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocketClient.closeConnection()
    }

    private fun scanWifi() {
        // Check if ACCESS_FINE_LOCATION permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_CODE)
            return
        }

        // Accessing WiFi scan results
        try {
            wifiManager.startScan()
            val wifiList: List<ScanResult> = wifiManager.scanResults
            val wifiDataList: MutableList<WifiData> = mutableListOf()
            for (result in wifiList) {
                if(!result.SSID.equals("")) {
                    val wifiData = WifiData(result.SSID, result.capabilities, result.frequency)
                    wifiDataList.add(wifiData)
                    Log.d("TAG", result.toString())
                }
            }
            val adapter = WifiDataAdapter(this, wifiDataList)
            listView.adapter = adapter
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    @SuppressLint("HardwareIds")
    private fun giveWifiInterfaceDetails() {
        val wifiInfo = wifiManager.connectionInfo
        infoText.append("SSID: ${wifiInfo.ssid}\n")
        infoText.append("MAC address: ${wifiInfo.macAddress}\n")
        infoText.append("Link speed: ${wifiInfo.linkSpeed} Mbps\n")
        infoText.append("Frequency: ${wifiInfo.frequency} MHz\n")
        infoText.append("RSSI: ${wifiInfo.rssi} dBm")
        Log.d("TAG", wifiInfo.toString())
    }

    private fun setOnWifiDeviceClickListener() {
        listView.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position) as WifiData
            val intent = Intent(this@WifiActivity, InfoActivity::class.java)
            intent.putExtra("MESSAGE_KEY", selectedItem)
            startActivity(intent)
        }

    }

    private fun setOnSendButtonListener() {
        sendButton.setOnClickListener {
            webSocketClient.sendMessage(textMessages.text.toString())
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            scanWifi()
            giveWifiInterfaceDetails()
        }
    }

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 100
    }
}
