package com.example.model.сlient

import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException


class SocketClient
{


    companion object
    {
        const val SOCKET_ADDRESS = "https://www.propolis.space"
    }

    private lateinit var mSocket:Socket


    @Synchronized
    fun setSocket()
    {
        try {
            mSocket = IO.socket(SOCKET_ADDRESS)
            println("Hello")
        } catch (e:URISyntaxException) {

        }
    }

    @Synchronized
    fun establishConnection()
    {
        mSocket.connect()
    }

    @Synchronized
    fun getSocket():Socket
    {
        return mSocket
    }


    fun closeConnection()
    {
        mSocket.disconnect()
    }


}