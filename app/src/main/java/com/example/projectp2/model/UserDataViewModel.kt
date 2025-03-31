package com.example.projectp2.model

import androidx.lifecycle.ViewModel

class UserDataViewModel : ViewModel() {
    var habits = mutableMapOf<Int, Habit>()
    val categories = mutableListOf(
        Category.PERSONAL,
        Category.WORK,
        Category.HEALTH,
        Category.OTHER
    )
    val frequencyTypes = mutableListOf(
        Frequency.DAILY,
        Frequency.WEEKLY,
        Frequency.MONTHLY,
        Frequency.YEARLY
    )

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

object Category {
    const val PERSONAL = "Personal"
    const val WORK = "Work"
    const val HEALTH = "Health"
    const val OTHER = "Other"
    const val NONE = "None"
}

object Frequency {
    const val DAILY = "Daily"
    const val WEEKLY = "Weekly"
    const val MONTHLY = "Monthly"
    const val YEARLY = "Yearly"
    const val NONE = "None"
}
