package com.anastasia.onboarding_app.di.modules

import com.anastasia.onboarding_app.data.repositories.OnboardingRepositoryImpl
import com.anastasia.onboarding_app.domain.repositories.OnboardingRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::OnboardingRepositoryImpl).bind<OnboardingRepository>()
}
