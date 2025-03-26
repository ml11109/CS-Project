package com.example.projectp2.data

import androidx.lifecycle.ViewModel

class AppViewModel : ViewModel() {
    var habits = mutableListOf<Habit>()

    fun getOngoingTasks(): ArrayList<Task> {
        return ArrayList()
    }

    fun getUpcomingTasks(): ArrayList<Task> {
        return ArrayList()
    }

    fun getHabitFromId(index: Int): Habit {
        return if (index == 0) {
            Habit.getEmpty()
        } else {
            Habit.getEmpty() // TODO
        }
    }
}