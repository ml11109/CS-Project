package com.example.projectp2.model

import java.time.LocalDate
import java.time.LocalTime

data class TaskList(
    var tasks: ArrayList<Task> = ArrayList(),
    var startDate: LocalDate = LocalDate.now(),
    var endDate: LocalDate = LocalDate.now(),
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
) {
    fun createTasks(habit: Habit) {
        tasks.clear()

        var date = startDate
        while (date <= endDate) {
            if (
                habit.frequency == Frequency.DAILY
                || (habit.frequency == Frequency.WEEKLY && daysOfWeek[date.dayOfWeek.value] == true)
                || (habit.frequency == Frequency.MONTHLY && daysOfMonth[date.dayOfMonth] == true)
            ) {
                for (i in startTimes.indices) {
                    tasks.add(Task(habit, startTimes[i], endTimes[i], date))
                }
            }
            date = date.plusDays(1)
        }
    }

    fun updateTasks(oldHabit: Habit, habit: Habit) {
        // If frequency has changed, creates new tasks
        // Otherwise, preserves only old tasks that have passed and are still within the time period
        // and creates new ones to fill in the new time period
        // Note that only new tasks will follow updated times
    }

    fun getProgress(): Float {
        return if (tasks.isEmpty()) 0f else tasks.filter { it.completed || it.exempted }.size.toFloat() / tasks.size.toFloat()
    }

    fun copy(habit: Habit): TaskList {
        val newTasks = ArrayList<Task>()
        for (task in tasks) {
            newTasks.add(task.copy(habit = habit))
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

    companion object {
        val daysOfWeekNames = mapOf(
            1 to "Monday",
            2 to "Tuesday",
            3 to "Wednesday",
            4 to "Thursday",
            5 to "Friday",
            6 to "Saturday",
            7 to "Sunday"
        )
    }
}
