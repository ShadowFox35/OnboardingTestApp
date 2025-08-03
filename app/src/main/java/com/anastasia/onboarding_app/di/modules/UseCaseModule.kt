package com.anastasia.onboarding_app.di.modules

import com.anastasia.onboarding_app.domain.use_cases.otp.SendOtpUseCase
import com.anastasia.onboarding_app.domain.use_cases.registration.SendUserDataUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val useCaseModule = module {
    factoryOf(::SendOtpUseCase)
    factoryOf(::SendUserDataUseCase)
}
