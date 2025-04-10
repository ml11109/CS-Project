package com.example.projectp2.util

import android.content.Context

fun setOnboardingSeen(context: Context) {
    val prefs = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
    prefs.edit().putBoolean("has_seen_onboarding", true).apply()
}

fun hasSeenOnboarding(context: Context): Boolean {
    val prefs = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
    return prefs.getBoolean("has_seen_onboarding", false)
}
