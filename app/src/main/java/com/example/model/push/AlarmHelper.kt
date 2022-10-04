package com.example.model.push

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log


class AlarmHelper(_context: Context)
{

    private val context = _context

    companion object
    {
        const val interval = 30000;
    }

    fun create()
    {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val triggerTime = System.currentTimeMillis() + interval;
        val alarmClockInfo = AlarmManager.AlarmClockInfo(triggerTime,null)
        alarmManager.setAlarmClock(alarmClockInfo,getAlarmActionPendingIntent())
        Log.d("Alarm:", "alarm set in $triggerTime")
    }


    private fun getAlarmActionPendingIntent(): PendingIntent {
        val intent = Intent(context, PushBroadcast::class.java)
        return PendingIntent.getBroadcast(context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE )
    }

}