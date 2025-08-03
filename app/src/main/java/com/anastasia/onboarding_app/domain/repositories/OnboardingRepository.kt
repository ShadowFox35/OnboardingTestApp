package com.anastasia.onboarding_app.domain.repositories

import com.anastasia.onboarding_app.domain.models.otp.OtpResultModel
import com.anastasia.onboarding_app.domain.models.registration.UserData

interface OnboardingRepository {
    suspend fun sendUserData(userData: UserData)
    suspend fun sendOtp(otp: String): OtpResultModel
}
