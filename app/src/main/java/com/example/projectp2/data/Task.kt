package com.example.projectp2.data

data class Task(
    var title: String
) {
    companion object {
        fun getEmpty(): Task {
            return Task("")
        }
    }
}
