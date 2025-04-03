package com.example.projectp2.model

import java.time.LocalDate
import java.time.LocalTime

data class Task(
    val habit: Habit,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val date: LocalDate,
    val completed: Boolean = false,
    val exempted: Boolean = false,
    val notes: String = ""
)
