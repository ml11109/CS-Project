package com.example.projectp2.model

import android.content.Context
import com.example.projectp2.util.scheduleTaskNotification
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime

data class TaskList(
    var tasks: ArrayList<Task> = ArrayList(),
    var startDate: LocalDate = LocalDate.now().plusDays(1),
    var endDate: LocalDate = LocalDate.now().plusDays(1),
    var startTimes: ArrayList<LocalTime> = ArrayList(),
    var endTimes: ArrayList<LocalTime> = ArrayList(),
    var daysOfWeek: HashMap<Int, Boolean> = HashMap<Int, Boolean>().apply {
        for (i in 1..7) {
            this[i] = false
        }
    },
    var daysOfMonth: HashMap<Int, Boolean> = HashMap<Int, Boolean>().apply {
        for (i in 1..31) {
            this[i] = false
        }
    }
) : Serializable {
    fun createTasks(context: Context, userDataViewModel: UserDataViewModel, habit: Habit) {
        tasks.clear()

        var index = 1
        var date = startDate
        while (date <= endDate) {
            if (
                habit.frequency == Frequency.DAILY
                || (habit.frequency == Frequency.WEEKLY && daysOfWeek[date.dayOfWeek.value] == true)
                || (habit.frequency == Frequency.MONTHLY && daysOfMonth[date.dayOfMonth] == true)
            ) {
                for (i in startTimes.indices) {
                    val task = Task(habit.id, index++, startTimes[i], endTimes[i], date)
                    tasks.add(task)
                    if (habit.advancedSettings.sendNotifications) {
                        scheduleTaskNotification(context, userDataViewModel, task, 15)
                    }
                }
            }
            date = date.plusDays(1)
        }
    }

    fun updateTasks(context: Context, userDataViewModel: UserDataViewModel, oldHabit: Habit, habit: Habit) {
        // If frequency has changed, creates new tasks
        // Otherwise, preserves only old tasks that have passed and are still within the time period
        // and creates new ones to fill in the new time period
        // Note that only new tasks will follow updated times
        createTasks(context, userDataViewModel, habit)
    }

    fun getProgress(): Float {
        return if (tasks.isEmpty()) 0f else tasks.filter { it.status in listOf(CompletionStatus.COMPLETED, CompletionStatus.SKIPPED) }.size.toFloat() / tasks.size.toFloat()
    }

    fun getNumExceptionsUsed(date: LocalDate): Int {
        var num = 0
        for (task in tasks) {
            if (task.date.month == date.month && task.status == CompletionStatus.SKIPPED) {
                num++
            }
        }
        return num
    }

    fun getFirstDate(habit: Habit): LocalDate? {
        // Gets first date with task given current task list parameters
        var date = startDate
        while (date <= endDate) {
            if (
                habit.frequency == Frequency.DAILY
                || (habit.frequency == Frequency.WEEKLY && daysOfWeek[date.dayOfWeek.value] == true)
                || (habit.frequency == Frequency.MONTHLY && daysOfMonth[date.dayOfMonth] == true)
            ) return date
            date = date.plusDays(1)
        }
        return null
    }

    fun copy(habit: Habit): TaskList {
        val newTasks = ArrayList<Task>()
        for (task in tasks) {
            newTasks.add(task.copy(habitId = habit.id))
        }

        val startTimes = ArrayList<LocalTime>()
        for (time in this.startTimes) {
            startTimes.add(LocalTime.from(time))
        }
        val endTimes = ArrayList<LocalTime>()
        for (time in this.endTimes) {
            endTimes.add(LocalTime.from(time))
        }

        return TaskList(
            newTasks,
            LocalDate.from(startDate),
            LocalDate.from(endDate),
            startTimes,
            endTimes,
            HashMap(daysOfWeek),
            HashMap(daysOfMonth)
        )
    }
}
