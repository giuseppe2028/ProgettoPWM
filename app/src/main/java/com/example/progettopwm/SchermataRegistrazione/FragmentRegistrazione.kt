package com.example.progettopwm.SchermataRegistrazione
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.widget.Toast
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
import com.example.progettopwm.ClientNetwork
import com.example.progettopwm.Login.OTPFragment
import com.example.progettopwm.R
import com.example.progettopwm.databinding.FragmentRegistrazioneBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

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

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       /* val intercector = HttpLoggingInterceptor()
        intercector.level= HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS).addInterceptor(intercector).build()
        val arg = Retrofit.Builder().baseUrl("http://10.0.2.2:8000/webmobile/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client).build().create(InterfacciaAPI::class.java)*/
        binding = FragmentRegistrazioneBinding.inflate(inflater)
        clickBottoni()



        val buttonConferma = binding.buttonRegistrati
        val nomeEditText = binding.editTextNome
        val cognomeEditText = binding.editTextCognome
        val emailEditText = binding.editTextEmail
        val passwordEditText = binding.editTextPassword
        val passwordEditTextC = binding.editTextTextPasswordConferma

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
            }

            override fun afterTextChanged(s: Editable?) {
                validatePasswords(passwordEditTextC)
            }
        })

        buttonConferma.setOnClickListener {
            if(validateOtherFields(nomeEditText, cognomeEditText, emailEditText, binding.textViewshowdata)){
                Toast.makeText(this.context,"I campi nome, cognome, mail e data nascita non sono riempiti",Toast.LENGTH_SHORT).show()
            }
            //controllo il contenuto della password
            else if(!validateOtherFields(nomeEditText, cognomeEditText, emailEditText, binding.textViewshowdata) && equalPasswords(passwordEditText,passwordEditTextC)){

                val nome = nomeEditText.text.toString()
                val cognome = cognomeEditText.text.toString()
                val email = emailEditText.text.toString()
                val data = binding.textViewshowdata.text.toString()
                val password = passwordEditText.text.toString()
                mostraOTP()
                caricaCredenziali(nome, cognome, email, data, password)
            }
        }


        return binding.root
    }
private fun mostraOTP(){
    val manager= parentFragmentManager
    val transaction = manager.beginTransaction()
    transaction.replace(R.id.fragmentContainerView, OTPFragment()).commit()
}


    private fun caricaCredenziali(nome: String, cognome: String, email: String, data: String, password: String){
        val query = "INSERT INTO Persona (nome,  cognome, mail, data_nascita, password) VALUES ('$nome', '$cognome', '$email', '$data', '$password')"

        ClientNetwork.retrofit.insert(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        Log.i("ciao", response.body().toString())
                    }
                    else{
                        Log.i("errore", "non funziona")
                        Log.i("errore", response.message())
                        Log.i("errore",  response.toString())

                    }
    }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("Errore", "Errore durante la chiamata di rete", t)
                    Toast.makeText(context, "Errore durante la chiamata di rete", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }




    private fun clickBottoni(){
        // Aggiungi il TextWatcher all'EditText della password
        binding.buttonData.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                // Aggiorna il TextView con la data selezionata
               val  selectedDate = "${selectedYear}-${selectedMonth + 1}-${selectedDay}"
                binding.textViewshowdata.text = convertStringToDate(selectedDate)
            }, year, month, day)

            datePickerDialog.show()
        }

    }

    private fun equalPasswords(passwordEditText: EditText, passwordEditTextC: EditText): Boolean {
        val password = passwordEditText.text.toString()
        val confirmPassword = passwordEditTextC.text.toString()
        Log.i("password", password)
        Log.i("confirm", confirmPassword)
        // Verifica se le password coincidono e se sono lunghe almeno 6 caratteri

        if (password.equals(confirmPassword)) {
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
    private fun validateOtherFields(EditText1: EditText, EditText2: EditText, EditText3: EditText, TextView: TextView): Boolean {
        val field1 = EditText1.text.toString().trim()
        val field2 = EditText2.text.toString().trim()
        val field3 = EditText3.text.toString().trim()
        val field4 = TextView.text.toString().trim()
        return field1.isEmpty() && field2.isEmpty() && field3.isEmpty() && field4.isEmpty()
    }
    fun convertStringToDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-M-dd")
        val outputFormat = SimpleDateFormat("yyyy-MM-dd")

        val date: Date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
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