package com.example.projectp2.model

import androidx.lifecycle.ViewModel

class UserDataViewModel : ViewModel() {
    var habits = mutableListOf<Habit>()
    val categories = mutableListOf("Work", "Personal", "Health")
    val frequencyTypes = mutableListOf("Daily", "Weekly", "Monthly")

    fun getOngoingTasks(): ArrayList<Task> {
        return ArrayList()
    }

    fun getUpcomingTasks(): ArrayList<Task> {
        return ArrayList()
    }

    fun getHabitFromId(index: Int): Habit {
        // TODO: Get habit from id
        return if (index == 0) {
            Habit()
        } else {
            Habit()
        }
    }
}
