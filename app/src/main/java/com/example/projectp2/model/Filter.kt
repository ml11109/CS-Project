package com.example.projectp2.model

import java.util.Calendar

data class Filter(
    var title: String? = null,
    var status: String? = null,
    var category: String? = null,
    var frequency: String? = null,
    var date: Calendar = Calendar.getInstance(),
    var filterDate: Boolean = false
) {
    companion object {
        fun filterHabits(habits: MutableCollection<Habit>): MutableList<Habit> {
            // TODO: Filter habits based on filter options
            val filteredHabits = mutableListOf<Habit>()
            filteredHabits.addAll(habits) // Temp
            return filteredHabits
        }
    }
}
