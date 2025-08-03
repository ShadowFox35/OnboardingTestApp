package com.anastasia.onboarding_app.ui.screens.registration

import android.app.DatePickerDialog
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Toast
import androidx.core.bundle.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anastasia.onboarding_app.R
import com.anastasia.onboarding_app.databinding.FragmentRegistrationScreenBinding
import com.anastasia.onboarding_app.ui.screens.otp.OtpScreen.Companion.EMAIL_ARG_KEY
import com.anastasia.onboarding_app.ui.screens.registration.model.EditTextType
import com.anastasia.onboarding_app.ui.screens.registration.model.RegistrationEffect
import com.anastasia.onboarding_app.ui.screens.registration.model.RegistrationEvent
import com.anastasia.onboarding_app.ui.screens.registration.model.RegistrationUiState
import com.anastasia.onboarding_app.utils.observeWithLifecycle
import com.google.android.material.textfield.TextInputEditText
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar


class RegistrationScreen : Fragment(R.layout.fragment_registration_screen) {

    private val binding by viewBinding(FragmentRegistrationScreenBinding::bind)
    val viewModel: RegistrationViewModel by viewModel()
    var originalPasswordTypeface: Typeface? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupListeners()
        setupObservers()
    }

    private fun setupViews() {
        binding.topBar.title.text = getString(R.string.registration_screen_app_bar_title)
        originalPasswordTypeface = binding.editTextPassword.typeface
    }

    private fun setupListeners() {
        binding.buttonContinue.setOnClickListener {
            viewModel.handleEvent(RegistrationEvent.OnContinueClick)
        }

        binding.editTextEmail.doOnTextChanged { text, _, _, _ ->
            viewModel.handleEvent(
                RegistrationEvent.OnEditTextUpdate(
                    text = text?.trim().toString(),
                    editTextType = EditTextType.Email,
                )
            )
        }

        binding.editTextBirth.setOnClickListener {
            showDateDialog()
        }

        binding.editTextPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.handleEvent(
                RegistrationEvent.OnEditTextUpdate(
                    text = text?.trim().toString(),
                    editTextType = EditTextType.Password,
                )
            )
        }

        binding.editTextConfirmPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.handleEvent(
                RegistrationEvent.OnEditTextUpdate(
                    text = text?.trim().toString(),
                    editTextType = EditTextType.ConfirmPassword,
                )
            )
        }

        binding.editTextPasswordLayout.setEndIconOnClickListener {
            togglePasswordVisibilityOnField(binding.editTextPassword)
            togglePasswordVisibilityOnField(binding.editTextConfirmPassword)
        }
    }

    private fun showDateDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val formatted = "%02d.%02d.%d".format(selectedDay, selectedMonth + 1, selectedYear)
                binding.editTextBirth.setText(formatted)
                viewModel.handleEvent(
                    RegistrationEvent.OnEditTextUpdate(
                        formatted,
                        EditTextType.DateOfBirth
                    )
                )
            },
            year, month, day
        )

        datePickerDialog.show()
    }

    private fun togglePasswordVisibilityOnField(editText: TextInputEditText) {
        with(editText) {
            val currentInputType = inputType
            inputType =
                if (currentInputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                } else {
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }

            originalPasswordTypeface?.let {
                editText.typeface = it
            }

            setSelection(text?.length ?: 0)
        }
    }

    private fun setupObservers() {
        viewModel.uiState.observeWithLifecycle(viewLifecycleOwner) { uiState ->
            renderState(uiState)
        }

        viewModel.uiEffect.observeWithLifecycle(viewLifecycleOwner) { uiEffect ->
            handleEffect(uiEffect)
        }
    }

    private fun renderState(uiState: RegistrationUiState) {
        binding.progress.root.visibility = if (uiState.loading) View.VISIBLE else View.GONE

        updateEmailValidation(uiState.isEmailValid)
    }

    private fun updateEmailValidation(isEmailValid: Boolean) {
        if (!isEmailValid) {
            binding.editTextEmailLayout.isErrorEnabled = true
            binding.editTextEmailLayout.error =
                getString(R.string.registration_screen_invalid_email)
        } else {
            binding.editTextEmailLayout.isErrorEnabled = false
        }
    }

    private fun handleEffect(uiEffect: RegistrationEffect) {
        when (uiEffect) {
            is RegistrationEffect.OpenOtpScreen -> openOtpScreen(uiEffect.email)
            RegistrationEffect.ShowErrorToast -> showErrorToast()
        }
    }

    private fun openOtpScreen(email: String) {
        val bundle = bundleOf(EMAIL_ARG_KEY to email)
        findNavController().navigate(R.id.open_otp_screen, bundle)
    }

    private fun showErrorToast() {
        Toast.makeText(
            this.context,
            R.string.registration_screen_error_text,
            Toast.LENGTH_SHORT
        ).show()
    }
}
