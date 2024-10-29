package com.example.serviceeg

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.serviceeg.ui.theme.ServiceEgTheme

class MainActivity : ComponentActivity() {

    private val CHANNEL_ID = "expanded_notification_channel"
    private val notificationId = 101

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        setContent {
            ServiceEgTheme {
                MainScreen(onShowNotification = { showExpandedNotification() })
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun showExpandedNotification() {
        val context = this

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val replyIntent = Intent(this, MainActivity::class.java)
        val replyPendingIntent = PendingIntent.getActivity(
            this,
            0,
            replyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val archiveIntent = Intent(this, MainActivity::class.java)
        val archivePendingIntent = PendingIntent.getActivity(
            this,
            0,
            archiveIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val replyAction = NotificationCompat.Action.Builder(
            0,
            "Responder",
            replyPendingIntent
        ).build()

        val archiveAction = NotificationCompat.Action.Builder(
            0,
            "Arquivar",
            archivePendingIntent
        ).build()

        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.avatar)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Nova Mensagem")
            .setContentText("Você recebeu uma nova mensagem.")
            .setLargeIcon(largeIcon)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(largeIcon)
                    .bigLargeIcon(null as Bitmap?)
            )
            .addAction(replyAction)
            .addAction(archiveAction)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, notification)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Canal de Notificações Expandidas"
            val descriptionText = "Canal para notificações expandidas"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

@Composable
fun MainScreen(onShowNotification: () -> Unit) {
    Button(onClick = onShowNotification) {
        Text(text = "Mostrar Notificação")
    }
}
