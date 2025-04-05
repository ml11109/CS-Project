package com.example.projectp2.model

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import java.time.LocalDateTime

class UserDataViewModel : ViewModel() {
    var habits = arrayListOf<Habit>()

    val statusTypes = arrayListOf(
        Status.ONGOING,
        Status.COMPLETED
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
            categoryColors[
                categories.indexOf(
                    category
                )
            ]
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

    fun getOngoingTasks(): List<Task> {
        // Returns tasks with the current time within its duration
        // sorted in ascending order by start time

        val ongoingTasks = ArrayList<Task>()
        for (habit in habits) {
            for (task in habit.taskList.tasks) {
                if (task.isOngoing()) {
                    ongoingTasks.add(task)
                }
            }
        }

        return ongoingTasks.sortedBy { it.startTime }
    }

    fun getCompletedTasks(numTasks: Int): List<Task> {
        // Gets tasks with the end time before the current time
        // and returns the first numTasks tasks, as sorted in descending order by end time

        val completedTasks = ArrayList<Task>()
        for (habit in habits) {
            for (task in habit.taskList.tasks) {
                if (task.isCompleted()) {
                    completedTasks.add(task)
                }
            }
        }

        completedTasks.sortByDescending { LocalDateTime.of(it.date, it.endTime) }
        return completedTasks.take(numTasks)
    }

    fun getUpcomingTasks(numTasks: Int): List<Task> {
        // Gets tasks with the start time after the current time
        // and returns the first numTasks tasks, as sorted in ascending order by start time

        val upcomingTasks = ArrayList<Task>()
        for (habit in habits) {
            for (task in habit.taskList.tasks) {
                if (task.isUpcoming()) {
                    upcomingTasks.add(task)
                }
            }
        }

        upcomingTasks.sortBy { LocalDateTime.of(it.date, it.startTime) }
        return upcomingTasks.take(numTasks)
    }
}

object Status {
    const val ALL = "All"
    const val ONGOING = "Ongoing"
    const val COMPLETED = "Completed"
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
