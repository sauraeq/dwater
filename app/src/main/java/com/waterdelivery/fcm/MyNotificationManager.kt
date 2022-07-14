package com.waterdelivery.fcm


import android.app.*
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.text.Html
import android.util.Log
import androidx.core.app.NotificationCompat
import com.waterdelivery.R

import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class MyNotificationManager(private val mCtx: Context) {
    var defaultSoundUri =
        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    val ID_BIG_NOTIFICATION = 234
    val ID_SMALL_NOTIFICATION = 235
//    var defaultSoundUri : Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    var time = Calendar.getInstance().timeInMillis



    //the method will show a big notification with an image
    //parameters are title for message title, message for message text, url of the big image and an intent that will open
    //when you will tap on the notification
    fun showBigNotification(message: String?, intent: Intent?, imageUrl: String?) {
        val resultPendingIntent = PendingIntent.getActivity(
            mCtx,
            ID_BIG_NOTIFICATION,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val bigPictureStyle = NotificationCompat.BigPictureStyle()
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString())
        Log.e("image url", imageUrl.toString())
        Log.e("time", time.toString())
        val bitmap = getBitmapfromUrl(imageUrl)
        val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
            mCtx,
            mCtx.getString(R.string.default_notification_channel_id)
        )
        val notification: Notification
        notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker(message).setWhen(0)
            .setAutoCancel(true)
            .setContentIntent(resultPendingIntent)
            .setContentTitle(mCtx.getString(R.string.app_name))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setSound(defaultSoundUri)
            .setContentText(message)
            .setLargeIcon(bitmap)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setWhen(time)
            .build()
        notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL
        val notificationManager =
            mCtx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId: String = mCtx.getString(R.string.default_notification_channel_id)
            val channel = NotificationChannel(
                channelId,
                mCtx.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = message
            notificationManager.createNotificationChannel(channel)
            mBuilder.setChannelId(channelId)
        }
        notificationManager.notify(ID_BIG_NOTIFICATION, notification)
    }

    //the method will show a small notification
    //parameters are title for message title, message for message text and an intent that will open
    //when you will tap on the notification
    fun showSmallNotification(message: String?, intent: Intent?) {
        val resultPendingIntent = PendingIntent.getActivity(
            mCtx,
            ID_SMALL_NOTIFICATION,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        Log.e("time", time.toString())
        val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
            mCtx,
            mCtx.getString(R.string.default_notification_channel_id)
        )
        val notification: Notification
        notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker(message).setWhen(0)
            .setAutoCancel(true)
            .setContentIntent(resultPendingIntent)
            .setContentTitle(mCtx.getString(R.string.app_name))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.mipmap.ic_launcher))
            .setSound(defaultSoundUri)
            .setContentText(message)
            .setWhen(time)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .build()
        notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL
        val notificationManager =
            mCtx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId: String = mCtx.getString(R.string.default_notification_channel_id)
            val channel = NotificationChannel(
                channelId,
                mCtx.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = message
            notificationManager.createNotificationChannel(channel)
            mBuilder.setChannelId(channelId)
        }
        notificationManager.notify(ID_BIG_NOTIFICATION, notification)
    }


    // Playing notification sound
    fun playNotificationSound() {
        try {
            val alarmSound: Uri = Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE
                        + "://" + mCtx.getPackageName() + "/raw/notification"
            )
            val r = RingtoneManager.getRingtone(mCtx, alarmSound)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /**
     * Method checks if the app is in background or not
     */
    fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            val runningProcesses = am.runningAppProcesses
            for (processInfo: RunningAppProcessInfo in runningProcesses) {
                if (processInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (activeProcess: String in processInfo.pkgList) {
                        if ((activeProcess == context.packageName)) {
                            isInBackground = false
                        }
                    }
                }
            }
        } else {
            val taskInfo = am.getRunningTasks(1)
            val componentInfo = taskInfo[0].topActivity
            if ((componentInfo!!.packageName == context.packageName)) {
                isInBackground = false
            }
        }
        return isInBackground
    }

    // Clears notification tray messages
    fun clearNotifications(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    /*
     *To get a Bitmap image from the URL received
     * */
    fun getBitmapfromUrl(imageUrl: String?): Bitmap? {
        try {
            Log.e("inside getbitmap", imageUrl.toString())
            val url = URL(imageUrl)
            val connection =
                url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            return BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            return null
        }
    }


}