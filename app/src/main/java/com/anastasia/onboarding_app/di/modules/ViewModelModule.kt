package com.anastasia.onboarding_app.di.modules

import com.anastasia.onboarding_app.ui.screens.otp.OtpViewModel
import com.anastasia.onboarding_app.ui.screens.registration.RegistrationViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::RegistrationViewModel)
    viewModelOf(::OtpViewModel)
}
