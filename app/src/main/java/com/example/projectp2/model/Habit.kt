package com.example.projectp2.model

data class Habit(
    var id: Int = 0,
    var title: String = "",
    var description: String = "",
    var category: String = Category.NONE,
    var frequency: String = Frequency.NONE,
    var taskList: TaskList = TaskList(),

    var sendNotifications: Boolean = false,
    var numExceptionsPerMonth: Int = 0,
    var allowExceptions: Boolean = false
)
