package com.anastasia.onboarding_app.ui.screens.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anastasia.onboarding_app.domain.use_cases.otp.SendOtpUseCase
import com.anastasia.onboarding_app.ui.screens.otp.model.OtpEffect
import com.anastasia.onboarding_app.ui.screens.otp.model.OtpEvent
import com.anastasia.onboarding_app.ui.screens.otp.model.OtpUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OtpViewModel(
    private val sendOtpUseCase: SendOtpUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OtpUiState.Initial)
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<OtpEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    fun handleEvent(event: OtpEvent) {
        when (event) {
            OtpEvent.OnBackClick -> navigateBack()
            is OtpEvent.UpdateEmail -> updateEmail(event.email)
            is OtpEvent.OnOtpUpdate -> updateOtp(event.otp)
            is OtpEvent.OnConfirmClick -> onConfirmButtonClick()
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _uiEffect.emit(OtpEffect.NavigateBack)
        }
    }

    private fun updateEmail(email: String) {
        _uiState.value = uiState.value.copy(email = email)
    }

    private fun updateOtp(otp: String) {
        _uiState.value = uiState.value.copy(
            otp = otp,
            isConfirmButtonEnabled = otp.length == OTP_SIZE
        )
    }

    private fun onConfirmButtonClick() {
        viewModelScope.launch {
            val result = sendOtpUseCase(uiState.value.otp)
            viewModelScope.launch {
                _uiEffect.emit(OtpEffect.ShowResultDialog(result.isValidOtp))
            }
        }
    }

    private companion object {
        const val OTP_SIZE = 6
    }
}
