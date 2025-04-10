package com.example.projectp2.util

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import com.example.projectp2.R
import com.example.projectp2.model.Task
import com.example.projectp2.model.UserDataViewModel
import java.time.LocalDateTime
import java.time.ZoneId

fun createNotificationChannel(context: Context) {
    val channel = NotificationChannel(
        "reminder_channel",
        "Task Reminders",
        NotificationManager.IMPORTANCE_HIGH
    )
    val manager = context.getSystemService(NotificationManager::class.java)
    manager.createNotificationChannel(channel)
}

@Composable
fun RequestNotificationPermission(onPermissionGranted: () -> Unit) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            Toast.makeText(context, "Notification permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val habitTitle = intent.getStringExtra("habitTitle") ?: "Your habit"
        val habitDescription = intent.getStringExtra("habitDescription") ?: ""

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, "reminder_channel")
            .setContentTitle("Upcoming task: $habitTitle")
            .setContentText(habitDescription)
            .setSmallIcon(R.drawable.app_icon_nobg)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}

fun scheduleTaskNotification(context: Context, userDataViewModel: UserDataViewModel, task: Task, minutesBefore: Long = 10L) {
    val dateTime = LocalDateTime.of(task.date, task.startTime)
    val adjustedTime = dateTime.minusMinutes(minutesBefore)
    val millis = adjustedTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

    if (millis < System.currentTimeMillis()) return // Skip if time is in the past

    val habit = userDataViewModel.getHabitFromId(task.habitId)
    val intent = Intent(context, NotificationReceiver::class.java).apply {
        putExtra("habitTitle", habit.title)
        putExtra("habitDescription", habit.description)
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        task.hashCode(), // Use unique ID per task
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.setAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        millis,
        pendingIntent
    )
}
