package com.anastasia.onboarding_app.ui.screens.otp.model

sealed interface OtpEffect {
    data class ShowResultDialog(val isSuccess: Boolean) : OtpEffect
    data object NavigateBack : OtpEffect
}
