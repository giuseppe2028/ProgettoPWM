package com.example.progettopwm.SchermataHome.FragmentPagine

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
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
import android.widget.Toast
import com.example.progettopwm.Gestione.ClientNetwork
import com.example.progettopwm.R
import com.example.progettopwm.databinding.FragmentModificaDatiBinding
import com.example.progettopwm.Gestione.idPersona
import com.example.progettopwm.SchermataHome.SchermataHome
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Date


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentModificaDati.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentModificaDati : Fragment() {
    private lateinit var binding: FragmentModificaDatiBinding
    private var isPasswordVisible: Boolean = false
    private var param1: String? = null
    private var param2: String? = null

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
    ): View? {




        binding = FragmentModificaDatiBinding.inflate(inflater)
        val buttonConferma = binding.buttonRegistrati
        val nomeEditText = binding.editTextNome
        val cognomeEditText = binding.editTextCognome
        val emailEditText = binding.editTextEmail
        val passwordEditText = binding.editTextPassword
        val passwordEditTextC = binding.editTextTextPasswordConferma


        val id_p= idPersona.getId()

       mostraDati(id_p)
       clickBottoni()




        //verifica la validità della nuova password
        passwordEditTextC.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                validatePasswords(passwordEditTextC)

            }
        })



        //questo blocco serve per l'occhiolino
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
        //fino a qui

        buttonConferma.setOnClickListener {
            if(validateOtherFields(nomeEditText, cognomeEditText, emailEditText, binding.textViewshowdata)){
                Toast.makeText(this.context,"I campi nome, cognome, mail e data nascita non sono riempiti",Toast.LENGTH_SHORT).show()
            }
            //controllo il contenuto della password
            else if(!validateOtherFields(nomeEditText, cognomeEditText, emailEditText, binding.textViewshowdata) && notEqualPasswords(passwordEditText,passwordEditTextC) && validatePasswords(passwordEditTextC)){
                EqualPasswords(passwordEditText, id_p) { equal ->
                    // Qui puoi gestire il risultato del confronto tra le password
                    if (equal) {
                        val nome = nomeEditText.text.toString()
                        val cognome = cognomeEditText.text.toString()
                        val email = emailEditText.text.toString()
                        val password = passwordEditTextC.text.toString()
                        val data= binding.textViewshowdata.text.toString()
                        val id_p = idPersona.getId()
                        inserisciDati(id_p, nome,cognome, data ,email, password)
                        startActivity(Intent(this.context, SchermataHome::class.java))
                    } else {
                        Toast.makeText(
                            this.context,
                            "ricontrollare i campi delle password",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                }}
            }

        return binding.root
    }

    fun convertStringToDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-M-dd")
        val outputFormat = SimpleDateFormat("yyyy-MM-dd")

        val date: Date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
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
                val selectedDate = "${selectedYear}-${selectedMonth + 1}-${selectedDay}"
                binding.textViewshowdata.text = convertStringToDate(selectedDate)
            }, year, month, day)

            datePickerDialog.show()
        }
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


    private fun validateOtherFields(EditText1: EditText, EditText2: EditText, EditText3: EditText, TextView: TextView): Boolean {
        val field1 = EditText1.text.toString().trim()
        val field2 = EditText2.text.toString().trim()
        val field3 = EditText3.text.toString().trim()
        val field4 = TextView.text.toString().trim()
        return field1.isEmpty() && field2.isEmpty() && field3.isEmpty() && field4.isEmpty()
    }
    private fun EqualPasswords(passwordEditText: EditText, id_p: Int, callback: (Boolean) -> Unit) {
        recuperaPassword(id_p) { password ->
            val pass = password.toString()
            val equal = pass.equals(passwordEditText.text.toString())
            callback(equal)
        }
    }


    private fun notEqualPasswords(passwordEditText: EditText, passwordEditTextC: EditText): Boolean {
        val password = passwordEditText.text.toString()
        val confirmPassword = passwordEditTextC.text.toString()
        // Verifica se le password non coincidono

        if (!password.equals(confirmPassword)) {
            passwordEditText.setBackgroundResource(R.drawable.edittext_border_green)
            passwordEditTextC.setBackgroundResource(R.drawable.edittext_border_green)
            return true
        }
        else{
            passwordEditText.setBackgroundResource(R.drawable.edittext_border_red)
            passwordEditTextC.setBackgroundResource(R.drawable.edittext_border_red)
            Toast.makeText(this.context, "Le password coincidono", Toast.LENGTH_SHORT).show()
            return false
        }

    }

    private fun validatePasswords(passwordEditText: EditText): Boolean {
        val password = passwordEditText.text.toString()
        val isPasswordValid = (password.length in 6..16)
        if (isPasswordValid) {
            passwordEditText.setBackgroundResource(R.drawable.edittext_border_green)
            return true
        }
        else{
            passwordEditText.setBackgroundResource(R.drawable.edittext_border_red)
            return false
        }
    }

    private fun mostraDati(id_p: Int){
        recuperaDati(id_p){result, nome,cognome,mail, data_nascita->
            if(result){
                binding.editTextNome.hint= nome.toString()
                binding.editTextCognome.hint = cognome.toString()
                binding.editTextEmail.hint = mail.toString()
                binding.textViewshowdata.text = data_nascita.toString()
            }
            else{
                binding.editTextNome.hint= "xxxxxxxxx"
                binding.editTextCognome.hint = "xxxxxxxxx"
                binding.editTextEmail.hint = "xxxxxx@xxxx.xxx"

            }
        }
    }


    private fun inserisciDati(id: Int, nome: String, cognome: String, data_nascita: String,mail: String,password: String){
        val query = "UPDATE  Persona SET nome = '$nome', cognome= '$cognome', data_nascita = '$data_nascita',mail='$mail', password = '$password' WHERE id = $id;"
        ClientNetwork.retrofit.update(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
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
    private fun recuperaPassword(id: Int, callback: (String?) -> Unit){
        val query = "SELECT password FROM Persona WHERE id = $id"
        ClientNetwork.retrofit.registrazione(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val resultSet = response.body()?.get("queryset") as JsonArray
                        if (resultSet.size() == 1) {
                            val password = resultSet[0].asJsonObject.get("password").asString
                            callback(password)
                        }else{
                            callback( null)
                        }
                    }
                    else{
                        callback(null)
                        Log.i("errore", "non funziona")
                        Log.i("errore", response.message())
                        Log.i("errore",  response.toString())
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    callback(null)
                    Log.e("Errore", "Errore durante la chiamata di rete", t)
                    Toast.makeText(context, "Errore durante la chiamata di rete", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }


    private fun recuperaDati(id: Int, callback: (Boolean, String?, String?, String?, String?) -> Unit){
        val query = "SELECT nome, cognome, mail, data_nascita FROM Persona WHERE id = $id"
        ClientNetwork.retrofit.registrazione(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val resultSet = response.body()?.get("queryset") as JsonArray
                        if (resultSet.size() == 1) {
                            val nome = resultSet[0].asJsonObject.get("nome").asString
                            val cognome = resultSet[0].asJsonObject.get("cognome").asString
                            val mail = resultSet[0].asJsonObject.get("mail").asString
                            val data_nascita = resultSet[0].asJsonObject.get("data_nascita").asString
                            callback(true, nome, cognome, mail, data_nascita)
                        }else{
                            callback(false, null, null, null, null)
                        }
                    }
                    else{
                        callback(false, null, null, null, null)
                        Log.i("errore", "non funziona")
                        Log.i("errore", response.message())
                        Log.i("errore",  response.toString())
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    callback(false, null, null, null, null)
                    Log.e("Errore", "Errore durante la chiamata di rete", t)
                    Toast.makeText(context, "Errore durante la chiamata di rete", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentModificaDati.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentModificaDati().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}