package com.anastasia.onboarding_app.utils

fun String.isEmailValid(): Boolean {
    val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    return emailRegex.matches(this)
}
