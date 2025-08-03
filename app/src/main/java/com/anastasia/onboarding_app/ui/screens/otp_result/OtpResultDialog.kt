package com.anastasia.onboarding_app.ui.screens.otp_result

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anastasia.onboarding_app.R
import com.anastasia.onboarding_app.databinding.FragmentOtpResultDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OtpResultDialog : BottomSheetDialogFragment(R.layout.fragment_otp_result_dialog) {

    private val binding by viewBinding(FragmentOtpResultDialogBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupListeners()
    }

    private fun setupView() {
        val argumentsBundle = arguments
        val isSuccess = argumentsBundle?.getBoolean(RESULT_ARG_KEY) ?: false
        val text = getString(
            if (isSuccess) {
                R.string.otp_bottom_sheet_result_success
            } else {
                R.string.otp_bottom_sheet_result_error
            }
        )
        val gif = if (isSuccess) {
            R.raw.success_gif
        } else {
            R.raw.fail_gif
        }

        binding.textResult.text = text
        binding.resultGift.gifResource = gif
    }

    private fun setupListeners() {
        binding.buttonDone.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        const val RESULT_ARG_KEY = "result"
    }
}