package com.example.projectp2.model

import java.time.LocalDate

data class Filter(
    var title: String = "",
    var status: String = Status.ALL,
    var category: String = Category.ALL,
    var frequency: String = Frequency.ALL,
    var date: LocalDate = LocalDate.now(),
    var filterDate: Boolean = false
) {
    fun filterHabits(habits: ArrayList<Habit>): ArrayList<Habit> {
        val filteredHabits = arrayListOf<Habit>()

        for (habit in habits) {
            var match = true
            if (this.title != "" && !habit.title.contains(this.title)) match = false
            if (this.status != Status.ALL && this.status != Status.ALL && habit.getStatus() != this.status) match = false
            if (this.category != Category.ALL && this.category != Category.ALL && habit.category != this.category) match = false
            if (this.frequency != Frequency.ALL && this.frequency != Frequency.ALL && habit.frequency != this.frequency) match = false
            if (this.filterDate && (this.date.isBefore(habit.taskList.startDate) || this.date.isAfter(habit.taskList.endDate))) match = false

            if (match) filteredHabits.add(habit)
        }

        return filteredHabits
    }
}
