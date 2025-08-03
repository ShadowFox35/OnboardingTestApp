package com.anastasia.onboarding_app

import android.app.Application
import com.anastasia.onboarding_app.di.modules.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class OnboardingApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@OnboardingApplication)
            modules(appModule)
        }
    }
}
