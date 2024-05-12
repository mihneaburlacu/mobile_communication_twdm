package com.example.myapplication2

import android.util.Log
import org.java_websocket.server.WebSocketServer
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import java.net.InetSocketAddress

class WebSocketServerApp(port: Int) : WebSocketServer(InetSocketAddress(port)) {
    private var messageCallback: MessageCallback? = null

    override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) {
        Log.d("TAG2", "New connection: ${conn?.remoteSocketAddress}")
        messageCallback?.onMessageReceived("\nNew connection: ${conn?.remoteSocketAddress}\n")
    }

    override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {
        Log.d("TAG2","Closed connection: ${conn?.remoteSocketAddress}, Code: $code, Reason: $reason")
        messageCallback?.onMessageReceived("\nClosed connection: ${conn?.remoteSocketAddress}\n")
    }

    override fun onMessage(conn: WebSocket?, message: String?) {
        Log.d("TAG2","Received message from ${conn?.remoteSocketAddress}: $message")
        messageCallback?.onMessageReceived("From ${conn?.remoteSocketAddress}: $message")
    }

    override fun onStart() {
        Log.d("TAG2","WebSocket server started")
    }

    override fun onError(conn: WebSocket?, ex: Exception?) {
        Log.d("TAG2","Error on connection ${conn?.remoteSocketAddress}: ${ex?.message}")
    }

    fun setMessageCallback(callback: MessageCallback) {
        messageCallback = callback
    }
}

