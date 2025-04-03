package com.example.projectp2.model

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class UserDataViewModel : ViewModel() {
    var habits = arrayListOf<Habit>()

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

    fun getOngoingTasks(): List<Task> {
        // Returns tasks with the current time within its duration
        // sorted in ascending order by start time

        val tasks = ArrayList<Task>()
        for (habit in habits) {
            tasks.addAll(habit.taskList.tasks)
        }

        val ongoingTasks = ArrayList<Task>()
        for (task in tasks) {
            if (
                task.date == LocalDate.now()
                && task.startTime.isBefore(LocalTime.now())
                && task.endTime.isAfter(LocalTime.now())
            ) {
                ongoingTasks.add(task)
            }
        }

        return ongoingTasks.sortedBy { it.startTime }
    }

    fun getUpcomingTasks(numTasks: Int): List<Task> {
        // Gets tasks with the start time after the current time
        // and returns the first numTasks tasks, as sorted in ascending order by start time

        val tasks = ArrayList<Task>()
        for (habit in habits) {
            tasks.addAll(habit.taskList.tasks)
        }

        val upcomingTasks = ArrayList<Task>()
        for (task in tasks) {
            if (
                task.date.isAfter(LocalDate.now())
                || (
                    task.date == LocalDate.now()
                    && task.startTime.isAfter(LocalTime.now())
                )
            ) {
                upcomingTasks.add(task)
            }
        }

        upcomingTasks.sortBy { LocalDateTime.of(it.date, it.startTime) }
        return upcomingTasks.take(numTasks)
    }
}

object Category {
    const val PERSONAL = "Personal"
    const val WORK = "Work"
    const val HEALTH = "Health"
    const val OTHER = "Other"
    const val NONE = "None"
}

object Frequency {
    const val DAILY = "Daily"
    const val WEEKLY = "Weekly"
    const val MONTHLY = "Monthly"
    const val NONE = "None"
}
