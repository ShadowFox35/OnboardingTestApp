package com.anastasia.onboarding_app.ui.screens.otp

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.view.ActionMode
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.core.bundle.bundleOf
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anastasia.onboarding_app.R
import com.anastasia.onboarding_app.databinding.FragmentOtpScreenBinding
import com.anastasia.onboarding_app.ui.screens.otp.model.OtpEffect
import com.anastasia.onboarding_app.ui.screens.otp.model.OtpEvent
import com.anastasia.onboarding_app.ui.screens.otp.model.OtpUiState
import com.anastasia.onboarding_app.ui.screens.otp_result.OtpResultDialog.Companion.RESULT_ARG_KEY
import com.anastasia.onboarding_app.utils.observeWithLifecycle
import org.koin.androidx.viewmodel.ext.android.viewModel

class OtpScreen : Fragment(R.layout.fragment_otp_screen) {

    private val binding by viewBinding(FragmentOtpScreenBinding::bind)
    val viewModel: OtpViewModel by viewModel()
    private lateinit var otpFields: List<EditText>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateEmail()
        setupViews()
        setupListeners()
        setupObservers()
    }

    private fun updateEmail() {
        val argumentsBundle = arguments
        val email = argumentsBundle?.getString(EMAIL_ARG_KEY) ?: ""
        viewModel.handleEvent(OtpEvent.UpdateEmail(email))
    }

    private fun setupViews() {
        binding.topBar.title.text = getString(R.string.otp_screen_app_bar_title)
        setUpOtpView()
    }

    private fun setupListeners() {
        binding.buttonConfirm.setOnClickListener {
            viewModel.handleEvent(OtpEvent.OnConfirmClick)
        }
        binding.topBar.backButton.root.setOnClickListener {
            viewModel.handleEvent(OtpEvent.OnBackClick)
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

    private fun renderState(uiState: OtpUiState) {
        binding.emailView.textEmailValue.text = uiState.email
        updateConfirmButton(uiState.isConfirmButtonEnabled)
    }

    private fun updateConfirmButton(isConfirmButtonEnabled: Boolean) {
        with(binding.buttonConfirm) {
            if (isConfirmButtonEnabled) {
                binding.buttonConfirm.isEnabled = true
                binding.buttonConfirm.alpha = 1f
            } else {
                binding.buttonConfirm.isEnabled = false
                binding.buttonConfirm.alpha = 0.5f
            }
        }
    }

    private fun handleEffect(uiEffect: OtpEffect) {
        when (uiEffect) {
            OtpEffect.NavigateBack -> findNavController().popBackStack()
            is OtpEffect.ShowResultDialog -> showResultDialog(uiEffect.isSuccess)
        }
    }

    private fun showResultDialog(isSuccess: Boolean) {
        val bundle = bundleOf(RESULT_ARG_KEY to isSuccess)
        findNavController().navigate(R.id.open_otp_result_dialog, bundle)
    }

    private fun setUpOtpView() {
        otpFields = listOf(
            binding.otpView.otp1,
            binding.otpView.otp2,
            binding.otpView.otp3,
            binding.otpView.otp4,
            binding.otpView.otp5,
            binding.otpView.otp6
        )

        otpFields.forEachIndexed { index, editText ->
            editText.filters = arrayOf(InputFilter.LengthFilter(1))
            editText.isCursorVisible = true

            editText.doAfterTextChanged {
                updateFieldBorder(editText, it)

                if (!it.isNullOrEmpty() && index < otpFields.lastIndex) {
                    otpFields[index + 1].requestFocus()
                }

                updateOtpCode(getOtpCode())
            }

            editText.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_DEL &&
                    editText.text.isEmpty() &&
                    index > 0
                ) {
                    otpFields[index - 1].setText("")
                    otpFields[index - 1].requestFocus()
                    true
                } else {
                    false
                }
            }

            @SuppressLint("ClickableViewAccessibility")
            editText.setOnTouchListener { view, _ ->
                val targetField = findTargetField()
                if (editText != targetField) {
                    targetField.requestFocus()
                    true
                } else {
                    view.performClick()
                    false
                }
            }

            editText.isLongClickable = false
            editText.setTextIsSelectable(false)
            editText.customSelectionActionModeCallback = object : ActionMode.Callback {
                override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?) = false
                override fun onCreateActionMode(mode: ActionMode?, menu: Menu?) = false
                override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?) = false
                override fun onDestroyActionMode(mode: ActionMode?) {}
            }
        }

        binding.otpView.root.setOnClickListener {
            val field = findTargetField()
            field.requestFocus()
        }

        otpFields[0].requestFocus()
    }

    private fun updateFieldBorder(editText: EditText, s: Editable?) {
        val backgroundRes =
            if (s.isNullOrEmpty()) R.drawable.otp_box_inactive_border else R.drawable.otp_box_active_border
        editText.background = ContextCompat.getDrawable(context, backgroundRes)
    }

    private fun findTargetField(): EditText {
        for (field in otpFields) {
            if (field.text.isEmpty()) return field
        }
        return otpFields.last()
    }

    private fun getOtpCode(): String {
        return otpFields.joinToString(separator = "") { it.text.toString() }
    }

    private fun updateOtpCode(code: String) {
        viewModel.handleEvent(OtpEvent.OnOtpUpdate(code))
    }

    companion object {
        const val EMAIL_ARG_KEY = "email"
    }
}
