package com.anastasia.onboarding_app.data.repositories

import com.anastasia.onboarding_app.domain.models.otp.OtpResultModel
import com.anastasia.onboarding_app.domain.models.registration.UserData
import com.anastasia.onboarding_app.domain.repositories.OnboardingRepository
import kotlinx.coroutines.delay

class OnboardingRepositoryImpl : OnboardingRepository {

    override suspend fun sendUserData(userData: UserData) {
        delay(5000L)
    }

    override suspend fun sendOtp(otp: String): OtpResultModel {
        return OtpResultModel(otp == "123456")
    }
}