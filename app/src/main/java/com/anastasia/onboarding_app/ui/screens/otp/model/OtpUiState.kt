package com.anastasia.onboarding_app.ui.screens.otp.model

data class OtpUiState(
    val isConfirmButtonEnabled: Boolean,
    val email: String,
    val otp: String,
) {
    companion object {
        val Initial = OtpUiState(
            isConfirmButtonEnabled = false,
            otp = "",
            email = "",
        )
    }
}
