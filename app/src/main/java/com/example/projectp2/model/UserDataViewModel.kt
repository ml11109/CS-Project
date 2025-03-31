package com.example.projectp2.model

import androidx.lifecycle.ViewModel

class UserDataViewModel : ViewModel() {
    var habits = mutableMapOf<Int, Habit>()
    val categories = mutableListOf("Work", "Personal", "Health")
    val frequencyTypes = mutableListOf("Daily", "Weekly", "Monthly")

    init {
        for (i in 0..10 step 2) {
            habits[i] = Habit()
        }
    }

    fun getOngoingTasks(): ArrayList<Task> {
        return ArrayList()
    }

    fun getUpcomingTasks(): ArrayList<Task> {
        return ArrayList()
    }

    fun getHabitFromId(id: Int): Habit {
        return if (id == -1) {
            // Get smallest available positive integer key
            // and create new habit with that key
            var newId = 0
            while (habits.containsKey(newId)) {
                newId ++
            }
            habits[newId] = Habit(id = newId)
            habits[newId]!!
        } else if (habits.containsKey(id)) {
            return habits[id]!!
        } else {
            return Habit(id = id)
        }
    }
}
