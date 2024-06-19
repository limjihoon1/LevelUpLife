package com.limjihoon.myhero

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.limjihoon.myhero.activitis.MainActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d("tokenCh", "Refreshed token: $token")

        sendRegistrationToServer(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // 푸시 메시지 수신 시 호출되는 메서드

        Log.d("FirebaseMessaging", "From: ${remoteMessage.from}")

        // 메시지 데이터 확인
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("FirebaseMessaging", "Message data payload: ${remoteMessage.data}")
            handleNow(remoteMessage.data)
        }

        // 알림 메시지 확인
        remoteMessage.notification?.let {
            Log.d("FirebaseMessaging", "Message Notification Body: ${it.body}")
            sendNotification(it.title, it.body)
        }
    }

    private fun sendNotification(title: String?, body: String?) {
        // 알림 생성 및 표시
        val intent = Intent(this, MainActivity::class.java) // 알림 클릭 시 이동할 액티비티
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val channelId = "my_channel_id" // 알림 채널 ID
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.fragment_icon_setting) // 알림 아이콘
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Oreo 이상에서는 알림 채널 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Channel Name", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

}

private fun sendRegistrationToServer(token: String) {
    Log.d("aaa", "sendRegistrationTokenToServer($token)")
}

private fun handleNow(data: Map<String, String>) {
    // 메시지 데이터를 즉시 처리해야 하는 경우 로직 추가
    // ...
}


