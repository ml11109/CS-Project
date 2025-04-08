package com.example.projectp2.model

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class UserDataViewModel : ViewModel() {
    var habits = arrayListOf(
        Habit("habit", "description", Category.PERSONAL, Frequency.DAILY,
            TaskList(ArrayList(), LocalDate.now(), LocalDate.now().plusDays(5),
                arrayListOf(LocalTime.now()), arrayListOf(LocalTime.now())
            )
        )
    )

    init {
        habits[0].taskList.createTasks(habits[0])
    }

    val statusTypes = arrayListOf(
        Status.ONGOING,
        Status.UPCOMING,
        Status.COMPLETED,
        Status.NOT_COMPLETED
    )

    val categories = arrayListOf(
        Category.PERSONAL,
        Category.WORK,
        Category.HEALTH,
        Category.OTHER
    )

    val categoryColors = arrayListOf(
        Color.Green,
        Color.Blue,
        Color.Red,
        Color.Gray
    )

    fun getCategoryColor(category: String): Color {
        return if (category in categories) {
            categoryColors[categories.indexOf(category)]
        } else {
            Color.White
        }
    }

    val frequencyTypes = arrayListOf(
        Frequency.DAILY,
        Frequency.WEEKLY,
        Frequency.MONTHLY
    )

    val habitTemplates = arrayListOf(
        Habit() // TODO: Create habit templates
    )

    fun getTasks(criterion: (Task) -> Boolean): List<Task> {
        val tasks = ArrayList<Task>()
        for (habit in habits) {
            for (task in habit.taskList.tasks) {
                if (criterion(task)) {
                    tasks.add(task)
                }
            }
        }
        return tasks
    }

    fun getAllTasks(): List<Task> {
        // Returns all tasks sorted in ascending order by start time

        return getTasks { true }.sortedBy { LocalDateTime.of(it.date, it.startTime) }
    }

    fun getOngoingTasks(numTasks: Int): List<Task> {
        // Returns tasks with the current time within its duration
        // sorted in ascending order by start time

        return getTasks { it.isOngoing() }.sortedBy { it.startTime }.take(numTasks)
    }

    fun getUpcomingTasks(numTasks: Int): List<Task> {
        // Gets tasks with the start time after the current time
        // and returns the first numTasks tasks, as sorted in ascending order by start time

        return getTasks { it.isUpcoming() }.sortedBy { LocalDateTime.of(it.date, it.startTime) }.take(numTasks)
    }

    fun getCompletedTasks(numTasks: Int): List<Task> {
        // Gets completed tasks with the end time before the current time
        // and returns the first numTasks tasks, as sorted in descending order by end time

        return getTasks { it.isCompleted() }.sortedByDescending { LocalDateTime.of(it.date, it.endTime) }.take(numTasks)
    }

    fun getNotCompletedTasks(numTasks: Int): List<Task> {
        // Gets not completed tasks with the end time before the current time
        // and returns the first numTasks tasks, as sorted in descending order by end time

        return getTasks { it.isNotCompleted() }.sortedByDescending { LocalDateTime.of(it.date, it.endTime) }.take(numTasks)
    }
}

object Status {
    const val ALL = "All"
    const val ONGOING = "Ongoing"
    const val UPCOMING = "Upcoming"
    const val NOT_COMPLETED = "Not Completed"
    const val COMPLETED = "Completed"
    const val NONE = "None"
}

object Category {
    const val ALL = "All"
    const val PERSONAL = "Personal"
    const val WORK = "Work"
    const val HEALTH = "Health"
    const val OTHER = "Other"
    const val NONE = "None"
}

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
