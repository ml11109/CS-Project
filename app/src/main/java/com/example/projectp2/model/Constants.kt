package com.example.projectp2.model

import androidx.compose.ui.graphics.Color

// For filtering habits
object HabitStatus {
    const val ALL = "All"
    const val ONGOING = "Ongoing"
    const val COMPLETED = "Completed"
}

// For filtering tasks
object TaskStatus {
    const val ALL = "All"
    const val ONGOING = "Ongoing"
    const val UPCOMING = "Upcoming"
    const val COMPLETED = "Completed"
    const val FAILED = "Failed"
}

// For marking tasks as completed
object CompletionStatus {
    const val PENDING = "Pending"
    const val COMPLETED = "Completed"
    const val SKIPPED = "Skipped"
}

object Category {
    const val ALL = "All"
    const val PERSONAL = "Personal"
    const val WORK = "Work"
    const val HEALTH = "Health"
    const val OTHER = "Other"
    const val NONE = "None"
}

val colors = arrayListOf(
    Color.Green,
    Color.Blue,
    Color.Red,
    Color.Gray,
    Color.Yellow,
    Color.Magenta,
    Color.Cyan,
    Color.Black,
    Color.White,
    Color.DarkGray,
    Color.LightGray
)

object Frequency {
    const val ALL = "All"
    const val DAILY = "Daily"
    const val WEEKLY = "Weekly"
    const val MONTHLY = "Monthly"
    const val NONE = "None"
}

object Week {
    val dayNames = mapOf(
        1 to "Monday",
        2 to "Tuesday",
        3 to "Wednesday",
        4 to "Thursday",
        5 to "Friday",
        6 to "Saturday",
        7 to "Sunday"
    )

    fun getDayName(dayOfWeek: Int): String {
        return dayNames[dayOfWeek] ?: ""
    }
}
