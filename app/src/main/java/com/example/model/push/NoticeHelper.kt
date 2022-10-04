package com.example.model.push

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import com.example.myapplication.MainActivity
import com.example.myapplication.R



class NoticeHelper(_context:Context)
{

    private val context = _context
    private val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var builder:Notification.Builder
    private var id = ID_START;


    companion object
    {
        const val CHANNEL_ID = "i.apps.propolis"
        const val DESCRIPTION = "Propolis notification"
        const val ID_START = 111;
    }


    private fun getPendingIntent():PendingIntent
    {
        val intent = Intent(context, MainActivity::class.java)

        return PendingIntent.getActivity(
            context,
            id,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }


    private fun show(notification: Notification)
    {
        notificationManager.notify(id++,notification)
    }


    fun create(username:String, message:String)
    {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel =
                    NotificationChannel(CHANNEL_ID, DESCRIPTION, NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.enableLights(true)
                notificationChannel.enableVibration(true)

                notificationManager.createNotificationChannel(notificationChannel)

                builder = Notification.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.logo)
                    .setColor(Color.BLACK)
                    .setContentTitle(username)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setContentIntent(getPendingIntent())
            } else {
                builder = Notification.Builder(context)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(username)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setContentIntent(getPendingIntent())
            }


       show(builder.build())
    }
}