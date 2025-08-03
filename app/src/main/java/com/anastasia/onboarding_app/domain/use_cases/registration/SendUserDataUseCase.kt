package com.anastasia.onboarding_app.domain.use_cases.registration

import com.anastasia.onboarding_app.domain.models.registration.UserData
import com.anastasia.onboarding_app.domain.repositories.OnboardingRepository

class SendUserDataUseCase(val repository: OnboardingRepository) {

    suspend operator fun invoke(userData: UserData) {
        repository.sendUserData(userData)
    }
}
