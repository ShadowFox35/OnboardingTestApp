package com.anastasia.onboarding_app.domain.use_cases.otp

import com.anastasia.onboarding_app.domain.models.otp.OtpResultModel
import com.anastasia.onboarding_app.domain.repositories.OnboardingRepository

class SendOtpUseCase(val repository: OnboardingRepository) {

    suspend operator fun invoke(otp: String): OtpResultModel {
        return repository.sendOtp(otp)
    }
}
