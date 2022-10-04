package com.example.model.push

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class PushBroadcast: BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?)
    {
        val intent = Intent(p0?.applicationContext, PushService::class.java)
        p0?.applicationContext?.startService(intent)
    }
}