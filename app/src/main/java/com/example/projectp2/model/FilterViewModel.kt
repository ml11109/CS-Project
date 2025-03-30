package com.example.projectp2.model

import androidx.lifecycle.ViewModel
import java.util.Calendar

class FilterViewModel : ViewModel() {
    var filter = Filter()
    val calendar = Calendar.getInstance()

    fun filterHabits(habits: MutableList<Habit>): MutableList<Habit> {
        // TODO: Filter habits based on filter options
        val filteredHabits = mutableListOf<Habit>()
        filteredHabits.addAll(habits) // Temp
        return filteredHabits
    }
}
