package com.example.projectp2.model

import androidx.lifecycle.ViewModel

class UserDataViewModel : ViewModel() {
    var habits = mutableListOf<Habit>()

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