package com.anastasia.onboarding_app.ui.screens.otp.model

sealed interface OtpEvent {
    data class OnOtpUpdate(val otp: String) : OtpEvent
    data object OnConfirmClick : OtpEvent
    data object OnBackClick : OtpEvent
    data class UpdateEmail(val email: String) : OtpEvent
}
