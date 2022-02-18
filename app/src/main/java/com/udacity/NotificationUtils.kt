package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

private const val NOTFICATION_ID = 0

fun NotificationManager.sendNotification(
    messageBody: String,
    applicationContext: Context,
    status: String,
    filename: String
) {
    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    val detailsIntent = Intent(applicationContext, DetailActivity::class.java)
    detailsIntent.putExtra("status", status)
    detailsIntent.putExtra("filename", filename)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT

    )
    val detailsPendingIntent = PendingIntent.getActivity(
        applicationContext,
        0,
        detailsIntent,
        PendingIntent.FLAG_UPDATE_CURRENT

    )

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.channel_name)
    ).setContentTitle("Your Downloads")
        .setContentText(messageBody)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(contentPendingIntent)
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .addAction(
            R.drawable.ic_assistant_black_24dp,
            "Look for details",
            detailsPendingIntent
        )

    notify(NOTFICATION_ID, builder.build())

}