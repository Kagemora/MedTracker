package com.yamazaki.medtracker.domain.model

data class AppSettings(
    val isDarkTheme: Boolean = false,
    val is24HourFormat: Boolean = true
)
