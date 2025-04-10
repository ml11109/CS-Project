package com.example.projectp2.model

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import com.example.projectp2.ui.theme.AppTheme
import com.example.projectp2.util.loadObjectList
import com.example.projectp2.util.loadTextFromFile
import com.example.projectp2.util.saveObjectList
import com.example.projectp2.util.saveTextToFile
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class UserDataViewModel : ViewModel() {
    var theme: AppTheme = AppTheme.System

    var habits = ArrayList<Habit>()

    fun getHabitId(): Int {
        // Obtain a unique ID
        return (habits.maxOfOrNull { it.id } ?: 0) + 1
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

    var statistics = arrayListOf(
        Statistic("Current Streak"),
        Statistic("Longest Streak"),
        Statistic("Habits Created"),
        Statistic("Habits Completed"),
        Statistic("Habit Completion Rate"),
        Statistic("Tasks Completed"),
        Statistic("Tasks Skipped"),
        Statistic("Tasks Failed"),
    )

    val achievements = arrayListOf(
        Achievement("First Created Habit", "Create your first habit", 1) {
            getStatisticValue("Habits Created")
        },
        Achievement("First Completed Task", "Complete your first task", 1) {
            getStatisticValue("Tasks Completed")
        },
        Achievement("First Failed Task", "Fail your first task", 1) {
            getStatisticValue("Tasks Failed")
        },
        Achievement("First Completed Habit", "Complete your first habit", 1) {
            getStatisticValue("Habits Completed")
        },
        Achievement("Habit Beginner", "Complete 10 habits", 10) {
            getStatisticValue("Habits Completed")
        },
        Achievement("Habit Master", "Complete 50 habits", 50) {
            getStatisticValue("Habits Completed")
        },
        Achievement("Streak Beginner", "Reach a streak of 10", 10) {
            getStatisticValue("Current Streak")
        },
        Achievement("Streak Master", "Reach a streak of 50", 50) {
            getStatisticValue("Current Streak")
        }
    )

    fun updateStatistic(description: String, value: Int, add: Boolean = true) {
        for (statistic in statistics) {
            if (statistic.description == description) {
                if (add) {
                    statistic.value += value
                } else {
                    statistic.value = value
                }
            }
        }
    }

    fun getStatisticValue(description: String): Int {
        for (statistic in statistics) {
            if (statistic.description == description) {
                return statistic.value
            }
        }
        return 0
    }

    fun getDaysSinceLastFailedTask(): Int {
        val failedTasks = getFailedTasks()
        if (failedTasks.isNotEmpty()) {
            val lastFailedTask = failedTasks.first()
            val today = LocalDateTime.now()
            val lastFailedDate = LocalDateTime.of(lastFailedTask.date, lastFailedTask.endTime)
            return ChronoUnit.DAYS.between(today, lastFailedDate).toInt()
        }
        return 0
    }

    fun getLongestStreak(): Int {
        var longestStreak = 0
        val failedTasks = getFailedTasks()
        if (failedTasks.isNotEmpty()) {
            for (i in 0 until failedTasks.size - 1) {
                val daysBetween = ChronoUnit.DAYS.between(
                    LocalDateTime.of(failedTasks[i].date, failedTasks[i].endTime),
                    LocalDateTime.of(failedTasks[i + 1].date, failedTasks[i + 1].endTime)
                ).toInt()
                longestStreak = maxOf(longestStreak, daysBetween)
            }
        }
        return longestStreak
    }

    fun updateStreak(context: Context) {
        updateStatistic("Current Streak", getDaysSinceLastFailedTask())
        updateStatistic("Longest Streak", getLongestStreak())
        saveStatistics(context)
    }

    fun updateHabitCompletion(context: Context) {
        updateStatistic("Tasks Completed", getCompletedTasks().size)
        updateStatistic("Tasks Skipped", getFailedTasks().size)
        updateStatistic("Tasks Failed", getFailedTasks().size)
        updateStatistic("Habits Completed", habits.filter { it.getStatus() == HabitStatus.COMPLETED }.size)
        updateStatistic("Habit Completion Rate", if (getStatisticValue("Habits Created") == 0) 0
            else getStatisticValue("Habits Completed") * 100 / getStatisticValue("Habits Created"))
        saveStatistics(context)
    }

    val habitTemplates = arrayListOf(
        Habit() // TODO: Create habit templates
    )

    fun loadData(context: Context) {
        loadTextFromFile(context, "theme.dat")?.let {
            theme = AppTheme.valueOf(it)
        }
        loadObjectList<Habit>(context, "habits.dat")?.let {
            habits = it
        }
        loadObjectList<String>(context, "categories.dat")?.let {
            categories = it
        }
        loadObjectList<Int>(context, "categoryColors.dat")?.let {
            categoryColors = it.map { color -> Color(color) } as ArrayList<Color>
        }
        loadObjectList<Statistic>(context, "statistics.dat")?.let {
            statistics = it
        }
    }

    fun saveTheme(context: Context) {
        saveTextToFile(context, "theme.dat", theme.name)
    }

    fun saveHabits(context: Context) {
        saveObjectList(context, habits, "habits.dat")
    }

    fun saveCategories(context: Context) {
        saveObjectList(context, categories, "categories.dat")
        saveObjectList(context, categoryColors.map { color -> color.toArgb() }, "categoryColors.dat")
    }

    fun saveStatistics(context: Context) {
        saveObjectList(context, statistics, "statistics.dat")
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
