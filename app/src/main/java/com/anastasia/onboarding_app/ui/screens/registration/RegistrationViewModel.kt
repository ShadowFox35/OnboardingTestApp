package com.anastasia.onboarding_app.ui.screens.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anastasia.onboarding_app.domain.models.registration.UserData
import com.anastasia.onboarding_app.domain.use_cases.registration.SendUserDataUseCase
import com.anastasia.onboarding_app.ui.screens.registration.model.EditTextType
import com.anastasia.onboarding_app.ui.screens.registration.model.RegistrationEffect
import com.anastasia.onboarding_app.ui.screens.registration.model.RegistrationEvent
import com.anastasia.onboarding_app.ui.screens.registration.model.RegistrationUiState
import com.anastasia.onboarding_app.utils.isEmailValid
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val sendUserDataUseCase: SendUserDataUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationUiState.Initial)
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<RegistrationEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    fun handleEvent(event: RegistrationEvent) {
        when (event) {
            is RegistrationEvent.OnEditTextUpdate -> updateUiState(event.text, event.editTextType)
            RegistrationEvent.OnContinueClick -> onContinueClick()
        }
    }

    private fun updateUiState(text: String, type: EditTextType) {
        when (type) {
            EditTextType.Email -> updateEmail(text)
            EditTextType.DateOfBirth -> _uiState.value = uiState.value.copy(birth = text)
            EditTextType.Password -> _uiState.value = uiState.value.copy(password = text)
            EditTextType.ConfirmPassword -> _uiState.value =
                uiState.value.copy(confirmPassword = text)
        }
    }

    private fun updateEmail(email: String) {
        val isEmailValid = if (email.isEmpty()) {
            true
        } else {
            email.isEmailValid()
        }

        _uiState.value = uiState.value.copy(
            email = email,
            isEmailValid = isEmailValid,
        )
    }

    private fun onContinueClick() {
        viewModelScope.launch {
            _uiState.value = uiState.value.copy(loading = true)
            if (!validateUiState()) {
                showError()
                _uiState.value = uiState.value.copy(loading = false)
                return@launch
            }

            val userData = UserData(
                email = uiState.value.email,
                password = uiState.value.password,
                birth = uiState.value.birth,
            )
            sendUserDataUseCase(userData)

            _uiState.value = uiState.value.copy(loading = false)
            openOtpScreen()
        }
    }

    private fun validateUiState(): Boolean {
        return with(uiState.value) {
            val isPasswordCorrect = password == confirmPassword && password.isNotEmpty()
            val isEmailCorrect = email.isNotEmpty() && isEmailValid
            birth.isNotEmpty() && isEmailCorrect && isPasswordCorrect
        }
    }

    private fun showError() {
        viewModelScope.launch {
            _uiEffect.emit(RegistrationEffect.ShowErrorToast)
        }
    }

    private fun openOtpScreen() {
        viewModelScope.launch {
            _uiEffect.emit(RegistrationEffect.OpenOtpScreen(uiState.value.email))
        }
    }
}
