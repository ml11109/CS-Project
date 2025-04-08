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

    val habitStatusTypes = arrayListOf(
        HabitStatus.ONGOING,
        HabitStatus.COMPLETED
    )

    val taskStatusTypes = arrayListOf(
        TaskStatus.ONGOING,
        TaskStatus.UPCOMING,
        TaskStatus.COMPLETED,
        TaskStatus.FAILED
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

    fun getTasks(filter: Filter = Filter(), criterion: (Task) -> Boolean): List<Task> {
        val tasks = ArrayList<Task>()
        for (habit in filter.filterHabits(habits)) {
            for (task in habit.taskList.tasks) {
                if (criterion(task)) {
                    tasks.add(task)
                }
            }
        }
        return tasks
    }

    fun getAllTasks(filter: Filter = Filter()): List<Task> {
        // Returns all tasks sorted in ascending order by start time

        return getTasks(filter) { true }.sortedBy { LocalDateTime.of(it.date, it.startTime) }
    }

    fun getOngoingTasks(filter: Filter = Filter()): List<Task> {
        // Returns pending tasks with the current time within its duration
        // sorted in ascending order by start time

        return getTasks(filter) { it.isOngoing() }.sortedBy { it.startTime }
    }

    fun getUpcomingTasks(filter: Filter = Filter()): List<Task> {
        // Returns pending tasks with the start time after the current time
        // sorted in ascending order by start time

        return getTasks(filter) { it.isUpcoming() }.sortedBy { LocalDateTime.of(it.date, it.startTime) }
    }

    fun getCompletedTasks(filter: Filter = Filter()): List<Task> {
        // Returns completed and skipped tasks sorted in descending order by end time

        return getTasks(filter) { it.isCompleted() }.sortedByDescending { LocalDateTime.of(it.date, it.endTime) }
    }

    fun getFailedTasks(filter: Filter = Filter()): List<Task> {
        // Returns pending tasks with the end time before the current time
        // sorted in descending order by end time

        return getTasks(filter) { it.isFailed() }.sortedByDescending { LocalDateTime.of(it.date, it.endTime) }
    }
}
