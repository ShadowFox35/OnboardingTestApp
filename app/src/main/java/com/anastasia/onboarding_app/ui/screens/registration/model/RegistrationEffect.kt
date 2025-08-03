package com.anastasia.onboarding_app.ui.screens.registration.model

sealed interface RegistrationEffect {
    data class OpenOtpScreen(val email: String) : RegistrationEffect
    data object ShowErrorToast : RegistrationEffect
}
