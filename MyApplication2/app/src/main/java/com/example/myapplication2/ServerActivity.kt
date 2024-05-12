package com.example.myapplication2

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.java_websocket.exceptions.WebsocketNotConnectedException

class ServerActivity : AppCompatActivity(), MessageCallback {

    private lateinit var webSocketServer: WebSocketServerApp
    private lateinit var messages: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)

        messages = findViewById(R.id.messages)

        // Start WebSocket server
        webSocketServer = WebSocketServerApp(8093) // Specify the port number
        webSocketServer.setMessageCallback(this) // Set callback in WebSocket server
        try {
            webSocketServer.start()
        } catch (e: Exception) {
            handleWebSocketException(e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            webSocketServer.stop()
        } catch (e: WebsocketNotConnectedException) {
            runOnUiThread {
                Toast.makeText(this, "Websocket not running", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMessageReceived(message: String) {
        // Update TextView with new message
        runOnUiThread {
            messages.append("$message\n")
        }
    }

    private fun handleWebSocketException(exception: Exception) {
        // Handle WebSocket exception here (e.g., show error message)
        exception.printStackTrace()
        runOnUiThread {
            Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
    }
}

