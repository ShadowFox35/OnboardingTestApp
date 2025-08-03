package com.anastasia.onboarding_app.ui.screens.registration.model

sealed interface RegistrationEvent {
    data object OnContinueClick : RegistrationEvent
    data class OnEditTextUpdate(val text: String, val editTextType: EditTextType) :
        RegistrationEvent
}
