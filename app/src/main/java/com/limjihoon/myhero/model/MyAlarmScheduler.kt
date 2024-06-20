package com.limjihoon.myhero.model

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar

class MyAlarmScheduler(private val context: Context) {

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleMidnightTask() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, MyBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 0) // 밤 12시 (24시 형식)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)

            // 만약 현재 시간이 이미 밤 12시를 지났다면 다음 날 밤 12시로 설정
            if (timeInMillis < System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

//        val calendar = Calendar.getInstance().apply {
//            timeInMillis = System.currentTimeMillis()
//            add(Calendar.MINUTE, 1) // 1분 뒤로 설정
//        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
}