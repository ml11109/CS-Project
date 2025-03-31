package com.example.projectp2.model

data class Habit(
    var id: Int = 0,
    var title: String = "",
    var description: String = "",
    var category: String = Category.NONE,
    var frequency: String = Frequency.NONE,
    var streak: Int = 0,
    var completion: Float = 0f,
    var daysCompleted: ArrayList<String> = ArrayList()
)
