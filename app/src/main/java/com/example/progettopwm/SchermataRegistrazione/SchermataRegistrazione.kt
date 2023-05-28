package com.example.progettopwm.SchermataRegistrazione

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.progettopwm.R
import com.example.progettopwm.databinding.ActivitySchermataRegistrazioneBinding

class SchermataRegistrazione : AppCompatActivity() {
    private lateinit var binding: ActivitySchermataRegistrazioneBinding
    private var isPasswordVisible = false


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchermataRegistrazioneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val passwordEditText = binding.editTextPassword
        val passwordEditTextC = binding.editTextTextPasswordConferma
        val buttonConferma = binding.buttonRegistrati
        val nomeEditText = binding.editTextNome
        val cognomeEditText = binding.editTextCognome
        val emailEditText = binding.editTextEmail
            // Aggiungi il TextWatcher all'EditText della password
        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                validatePasswords(passwordEditText, passwordEditTextC)
            }
        })
        // Aggiungi il TextWatcher all'EditText della conferma della password
        passwordEditTextC.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                validatePasswords(passwordEditText, passwordEditTextC)
            }
        })
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

        buttonConferma.setOnClickListener {
            if (!validatePasswords(passwordEditText, passwordEditTextC) || !validateOtherFields(nomeEditText, cognomeEditText, emailEditText)) {
                Toast.makeText(this, "Controllare il contenuto dei campi", Toast.LENGTH_SHORT).show()
            }
            else{
                //inserire il comportamento del bottone
            }
        }


    }





    private fun validatePasswords(passwordEditText: EditText,passwordEditTextC: EditText ): Boolean {
        val password = passwordEditText.text.toString()
        val confirmPassword = passwordEditTextC.text.toString()

        // Verifica se le password coincidono e se sono lunghe almeno 6 caratteri
        val passwordsMatch = password == confirmPassword
        val isPasswordValid = password.length >= 6
        val isPasswordValidC = confirmPassword.length >= 6


        if (passwordsMatch && isPasswordValid && isPasswordValidC) {
            passwordEditTextC.setTextColor(Color.GREEN)
            passwordEditText.setTextColor(Color.GREEN)
            passwordEditText.setBackgroundResource(R.drawable.edittext_border_green)
            passwordEditTextC.setBackgroundResource(R.drawable.edittext_border_green)
            return true
        }else{passwordEditText.setTextColor(Color.RED)
            passwordEditTextC.setTextColor(Color.RED)
            passwordEditText.setBackgroundResource(R.drawable.edittext_border_red)
            passwordEditTextC.setBackgroundResource(R.drawable.edittext_border_red)
            return false
        }

    }

    private fun validateOtherFields(EditText1: EditText,EditText2: EditText,EditText3: EditText): Boolean {
        val field1 = EditText1.text.toString().trim()
        val field2 = EditText2.text.toString().trim()
        val field3 = EditText3.text.toString().trim()
        return field1.isNotEmpty() && field2.isNotEmpty() && field3.isNotEmpty()
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