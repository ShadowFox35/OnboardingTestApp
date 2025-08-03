package com.anastasia.onboarding_app.ui.screens.registration.model

data class RegistrationUiState(
    val email: String,
    val birth: String,
    val password: String,
    val confirmPassword: String,
    val isEmailValid: Boolean,
    val loading: Boolean,
) {
    companion object {
        val Initial = RegistrationUiState(
            email = "",
            birth = "",
            password = "",
            isEmailValid = true,
            confirmPassword = "",
            loading = false,
        )
    }
}
