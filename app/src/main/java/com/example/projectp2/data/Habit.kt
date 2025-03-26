package com.example.projectp2.data

data class Habit(
    var title: String
) {
    companion object {
        fun getEmpty(): Habit {
            return Habit("")
        }
    }
}
