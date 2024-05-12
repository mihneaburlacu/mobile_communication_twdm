package com.example.myapplication

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketClientApp {
    private val client = OkHttpClient()
    private val request = Request.Builder().url("ws://localhost:8093").build()
    private val webSocket = client.newWebSocket(request, object : WebSocketListener() {
        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("TAG1", "receivd message: $text")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("TAG1", "Closed communication because: $reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
            Log.d("TAG1", "Failure websocket $response $t")
        }
    })

    fun sendMessage(message: String) {
        Log.d("TAG1", "sending message: $message")
        webSocket.send(message)
    }

    fun closeConnection() {
        webSocket.close(1000, "Connection closed")
    }
}
