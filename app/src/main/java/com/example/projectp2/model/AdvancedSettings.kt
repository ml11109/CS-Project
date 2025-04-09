package com.example.projectp2.model

import java.io.Serializable

data class AdvancedSettings(
    var sendNotifications: Boolean = true,
    var numExceptionsPerMonth: Int = 0,
    var allowExceptions: Boolean = false,
    var allowAdvanceCompletion: Boolean = false
) : Serializable
