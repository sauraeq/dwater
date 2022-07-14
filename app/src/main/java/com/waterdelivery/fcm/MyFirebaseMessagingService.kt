
package com.waterdelivery.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.waterdelivery.R


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "MyFirebaseMsgService"

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        println("FCM Token $s")
        Log.d(TAG, "onNewToken: $s")


    }

    /*override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.e(TAG, "From: " + remoteMessage.notification)
        Log.e(TAG, "From: " + remoteMessage.data)
        if (remoteMessage.data.size > 0) {
            val data = remoteMessage.data
            if (data != null && data.containsKey("identifier")) {
                val pushNotification: In tent
                val message = data["msg"]
                if (!TextUtils.isEmpty(
                                AppPreferences.getInstance(this)
                                        .personDetail.id
                        )
                ) {
                    pushNotification = Intent(applicationContext, NotificationActivity::class.java)
                    pushNotification.putExtra("isNotificationTap", true)
                    pushNotification.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                } else {
                    pushNotification = Intent(applicationContext, SplashActivity::class.java)
                    pushNotification.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    pushNotification.putExtra("message", message)
                }
                pushNotification.putExtra("message", message)
                val mNotificationManager = MyNotificationManager(
                        applicationContext
                )
                mNotificationManager.showSmallNotification(message, pushNotification)

//                handleDataPayload(data, remoteMessage.getNotification().getBody());
            }
        }
        if (remoteMessage.notification != null) {
            Log.e(
                    TAG, "Message NotificationActivity Body: " + remoteMessage.notification!!
                    .body
            )
        }
    }*/

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        sendNotification(remoteMessage);
    }

    private fun sendNotification(remoteMessage: RemoteMessage ) {
        val intent = Intent(this, NotificationActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,
                0, intent, PendingIntent.FLAG_ONE_SHOT)


        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.appicon)
                    .setContentTitle(remoteMessage.notification?.title)
                    .setContentText(remoteMessage.notification?.body)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build());



    }


}
