package com.example.progettopwm.SchermataRegistrazione
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.widget.Toast
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.progettopwm.Login.OTPFragment
import com.example.progettopwm.R
import com.example.progettopwm.databinding.FragmentRegistrazioneBinding
import java.util.Calendar

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentRegistrazione.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentRegistrazione : Fragment() {
    private lateinit var binding : FragmentRegistrazioneBinding
    private var param1: String? = null
    private var param2: String? = null
    private var isPasswordVisible: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrazioneBinding.inflate(inflater)

        val passwordEditText = binding.editTextPassword
        val passwordEditTextC = binding.editTextTextPasswordConferma
        val buttonConferma = binding.buttonRegistrati
        val nomeEditText = binding.editTextNome
        val cognomeEditText = binding.editTextCognome
        val emailEditText = binding.editTextEmail
        // Aggiungi il TextWatcher all'EditText della password
        binding.buttonData.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                // Aggiorna il TextView con la data selezionata
                val selectedDate = "${selectedDay}/${selectedMonth + 1}/${selectedYear}"
                binding.textViewshowdata.text = selectedDate
            }, year, month, day)

            datePickerDialog.show()
        }

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                validatePasswords(passwordEditText)

            }
        })
        // Aggiungi il TextWatcher all'EditText della conferma della password
        passwordEditTextC.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePasswordsC(passwordEditText)
            }

            override fun afterTextChanged(s: Editable?) {
                validatePasswordsC(passwordEditText)

            }
        })
        passwordEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                passwordEditText.hint = resources.getString(R.string.password)
            } else {
                passwordEditText.hint = null
            }
        }
        passwordEditTextC.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                passwordEditTextC.hint = resources.getString(R.string.password)
            } else {
                passwordEditTextC.hint = null
            }
        }
        passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_24, 0)
        passwordEditTextC.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_24, 0)
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
                val drawableEnd = passwordEditTextC.compoundDrawables[2]
                if (drawableEnd != null && event.rawX >= passwordEditTextC.right - drawableEnd.bounds.width()) {
                    togglePasswordVisibility(passwordEditTextC)
                    return@setOnTouchListener true
                }
            }
            false
        }

        buttonConferma.setOnClickListener {
            if (!validatePasswords(passwordEditText) || !validateOtherFields(nomeEditText, cognomeEditText, emailEditText, binding.textViewshowdata)||equalPasswords(passwordEditText, passwordEditTextC)) {
                Toast.makeText(this.context, "Controllare il contenuto dei campi", Toast.LENGTH_SHORT).show()

            }
            else{
                //inserire il comportamento del bottone
                clickBottoni()
            }
        }


        return binding.root
    }

    private fun clickBottoni() {
        binding.buttonRegistrati.setOnClickListener{
            val manager= parentFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, OTPFragment()).commit()
        }
    }

    private fun equalPasswords(passwordEditText: EditText, passwordEditTextC: EditText): Boolean {
        val password = passwordEditText.text.toString()
        val confirmPassword = passwordEditTextC.text.toString()
        Log.i("password", password)
        Log.i("confirm", confirmPassword)
        // Verifica se le password coincidono e se sono lunghe almeno 6 caratteri

        if (password == confirmPassword) {
            //passwordEditTextC.setTextColor(Color.GREEN)
            //passwordEditText.setTextColor(Color.GREEN)
            passwordEditText.setBackgroundResource(R.drawable.edittext_border_green)
            passwordEditTextC.setBackgroundResource(R.drawable.edittext_border_green)

            return true
        }
        else{
            passwordEditText.setBackgroundResource(R.drawable.edittext_border_red)
            passwordEditTextC.setBackgroundResource(R.drawable.edittext_border_red)
            Toast.makeText(this.context, "Le password non coincidono", Toast.LENGTH_SHORT).show()

            return false
        }

    }

    private fun validatePasswords(passwordEditText: EditText): Boolean {
        val password = passwordEditText.text.toString()

        // Verifica se le password coincidono e se sono lunghe almeno 6 caratteri
        val isPasswordValid = password.length >= 6


        if (isPasswordValid) {
            //passwordEditTextC.setTextColor(Color.GREEN)
            //passwordEditText.setTextColor(Color.GREEN)
            passwordEditText.setBackgroundResource(R.drawable.edittext_border_green)
            return true
        }
        else{
            passwordEditText.setBackgroundResource(R.drawable.edittext_border_red)
            return false
        }

    }
    private fun validatePasswordsC( passwordEditTextC: EditText): Boolean {
        val confirmPassword = passwordEditTextC.text.toString()

        // Verifica se le password coincidono e se sono lunghe almeno 6 caratteri
         confirmPassword.length >= 6

        if (confirmPassword.length >= 6) {

            //passwordEditTextC.setTextColor(Color.GREEN)
            //passwordEditText.setTextColor(Color.GREEN)
            passwordEditTextC.setBackgroundResource(R.drawable.edittext_border_green)
            return true
        }
        else{
            passwordEditTextC.setBackgroundResource(R.drawable.edittext_border_red)
            return false
        }
    }

    private fun validateOtherFields(EditText1: EditText, EditText2: EditText, EditText3: EditText, TextView: TextView): Boolean {
        val field1 = EditText1.text.toString().trim()
        val field2 = EditText2.text.toString().trim()
        val field3 = EditText3.text.toString().trim()
        val field4 = TextView.text.toString().trim()
        return field1.isNotEmpty() && field2.isNotEmpty() && field3.isNotEmpty() && field4.isNotEmpty()
    }

    private fun togglePasswordVisibility(passwordEdi: EditText) {
        isPasswordVisible = !isPasswordVisible
        if (isPasswordVisible) {
            passwordEdi.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            passwordEdi.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_off_24, 0)
        } else {
            passwordEdi.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            passwordEdi.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_24, 0)
        }
        passwordEdi.setSelection(passwordEdi.text.length)
    }




    // Inflate the layout for this fragment



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentRegistrazione.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentRegistrazione().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}