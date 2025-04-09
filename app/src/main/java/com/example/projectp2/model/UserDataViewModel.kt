package com.example.projectp2.model

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import com.example.projectp2.util.loadObjectList
import com.example.projectp2.util.saveObjectList
import java.time.LocalDateTime

class UserDataViewModel : ViewModel() {
    var habits = ArrayList<Habit>()

    fun getHabitId(): Int {
        val ids = setOf<Int>()
        for (habit in habits) {
            ids.plusElement(habit.id)
        }

        var id = 0
        while (id in ids) {
            id++
        }
        return id
    }

    fun getHabitFromId(id: Int): Habit {
        for (habit in habits) {
            if (habit.id == id) {
                return habit
            }
        }
        return Habit()
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

    var categories = arrayListOf(
        Category.PERSONAL,
        Category.WORK,
        Category.HEALTH,
        Category.OTHER
    )

    var categoryColors = arrayListOf(
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

    fun loadData(context: Context) {
        loadObjectList<Habit>(context, "habits.dat")?.let {
            habits = it
        }
        loadObjectList<String>(context, "categories.dat")?.let {
            categories = it
        }
        loadObjectList<Int>(context, "categoryColors.dat")?.let {
            categoryColors = it.map { color -> Color(color) } as ArrayList<Color>
        }
    }

    fun saveHabits(context: Context) {
        saveObjectList(context, habits, "habits.dat")
    }

    fun saveCategories(context: Context) {
        saveObjectList(context, categories, "categories.dat")
        saveObjectList(context, categoryColors.map { color -> color.toArgb() }, "categoryColors.dat")
    }

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
