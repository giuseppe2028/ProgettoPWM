package com.example.progettopwm.SchermataRegistrazione

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import com.example.progettopwm.R
import com.example.progettopwm.databinding.ActivitySchermataRegistrazioneBinding

class SchermataRegistrazione : AppCompatActivity() {
    private lateinit var binding: ActivitySchermataRegistrazioneBinding
    private var isPasswordVisible = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchermataRegistrazioneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val passwordEditText = binding.editTextPassword
        val passwordEditTextC = binding.editTextTextPasswordConferma

        passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_off_24, 0)
        passwordEditTextC.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_off_24, 0)
        passwordEditText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = passwordEditText.compoundDrawables[2]
                if (drawableEnd != null && event.rawX >= passwordEditText.right - drawableEnd.bounds.width()) {
                    togglePasswordVisibility(passwordEditText)
                    return@setOnTouchListener true
                }
            }
            false
        }
        passwordEditTextC.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = passwordEditText.compoundDrawables[2]
                if (drawableEnd != null && event.rawX >= passwordEditText.right - drawableEnd.bounds.width()) {
                    togglePasswordVisibility(passwordEditTextC)
                    return@setOnTouchListener true
                }
            }
            false
        }
    }


    private fun togglePasswordVisibility(passwordEditText: EditText) {
        isPasswordVisible = !isPasswordVisible
        if (isPasswordVisible) {
            passwordEditText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_24, 0)
        } else {
            passwordEditText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_off_24, 0)
        }
        passwordEditText.setSelection(passwordEditText.text.length)
    }




}