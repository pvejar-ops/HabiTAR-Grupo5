package com.example.habitar.core.notificaciones

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.habitar.R

class NotificacionWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Crear canal (obligatorio en Android 8+)
        val canal = NotificationChannel(
            "habitar_canal",
            "HabiTAR",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        manager.createNotificationChannel(canal)

        val notificacion = NotificationCompat.Builder(context, "habitar_canal")
            .setSmallIcon(R.drawable.ic_notificacion)
            .setContentTitle("HabiTAR")
            .setContentText("HabiTAR te acompaña 🌟")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        manager.notify(1, notificacion)
        return Result.success()
    }
}