package com.example.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.MainActivity
import com.example.R

object NotificationHelper {

    const val CHANNEL_ID = "fatanverse_chapter_updates"
    const val CHANNEL_NAME = "Update Bab FATANVERSE"
    const val CHANNEL_DESC = "Pemberitahuan resmi saat bab cerita atau arc terbaru dirilis."
    const val NOTIFICATION_ID = 1001

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESC
                enableVibration(true)
                enableLights(true)
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendChapterUpdateNotification(
        context: Context,
        chapterTitle: String,
        arcSubtitle: String,
        targetChapterId: String = "arc_4_preview"
    ): Boolean {
        createNotificationChannel(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("TARGET_CHAPTER_ID", targetChapterId)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.img_app_icon)
            .setContentTitle("🔥 Rilis Baru: $chapterTitle")
            .setContentText(arcSubtitle)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("$arcSubtitle\n\nSensor DHC menangkap anomali energi di bawah tanah Sektor 4! Buka sekarang untuk membaca teaser kelanjutan FATANVERSE.")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        return try {
            val manager = NotificationManagerCompat.from(context)
            manager.notify(NOTIFICATION_ID, builder.build())
            true
        } catch (e: SecurityException) {
            // Permission not granted on Android 13+
            false
        }
    }
}
