package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class InfoActivity : AppCompatActivity() {
    private lateinit var infoText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.info_activity)

        infoText = findViewById(R.id.info_text)

        val wifiData = intent.getParcelableExtra<WifiData>("MESSAGE_KEY")
        infoText.text = "SSID: ${wifiData?.ssid}\n\nCapabilities: ${wifiData?.capabilities}\n\nFrequency: ${wifiData?.frequency} MHz"
    }
}