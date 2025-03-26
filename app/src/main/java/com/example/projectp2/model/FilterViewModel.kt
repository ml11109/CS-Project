package com.example.projectp2.model

import androidx.lifecycle.ViewModel

class FilterViewModel : ViewModel() {
    var filter = Filter()

    fun filterHabits(habits: MutableList<Habit>): MutableList<Habit> {
        // TODO: Filter habits based on filter options
        val filteredHabits = mutableListOf<Habit>()
        return filteredHabits
    }
}