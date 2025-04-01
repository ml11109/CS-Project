package com.example.projectp2.model

import java.time.LocalTime

data class TaskList(
    var frequency: String = Frequency.NONE,
    var daysOfWeek: ArrayList<Int> = ArrayList(),
    var daysOfMonth: ArrayList<Int> = ArrayList(),
    var startTimes: ArrayList<LocalTime> = ArrayList(),
    var endTimes: ArrayList<LocalTime> = ArrayList(),
    var tasks: ArrayList<Task> = ArrayList(),
) {
    companion object {
        val daysOfWeek = mapOf(
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
