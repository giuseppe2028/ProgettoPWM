package com.example.progettopwm.SchermataHome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import com.example.progettopwm.R
import com.example.progettopwm.databinding.ActivitySchermataInserimentoDatiPagamentoBinding
import com.example.progettopwm.databinding.ActivitySchermataModificaDatiAccountBinding

class SchermataInserimentoDatiPagamento : AppCompatActivity() {
    private lateinit var binding: ActivitySchermataInserimentoDatiPagamentoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchermataInserimentoDatiPagamentoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.editTextCvv.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(3))
        binding.editTextmese.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(2))
        binding.editTextanno.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(2))
        binding.editTextnumero.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(16))

        //impongo che non si possono inserire piu di un tot di caratteri dentro il plaintext
        binding.editTextCvv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
             /*   if (s?.length != 3) {
                    binding.editTextCvv.setBackgroundResource(R.drawable.edittext_border_red)
                } else {
                    binding.editTextCvv.setBackgroundResource(R.drawable.edittext_border_green)
                }*/
            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length != 3) {
                    binding.editTextCvv.setBackgroundResource(R.drawable.edittext_border_red)
                }
            }
        })

        binding.editTextmese.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               /* if (s?.length != 2) {
                    binding.editTextmese.setBackgroundResource(R.drawable.edittext_border_red)
                } else {
                    binding.editTextmese.setBackgroundResource(R.drawable.edittext_border_green)
                }*/
            }

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                if (input.length == 2) {
                    val month = input.toIntOrNull()
                    if(month != null){
                    if ( month < 1 || month > 12) {
                        binding.editTextmese.setBackgroundResource(R.drawable.edittext_border_red)
                    } }
                }

            }
        })

        binding.editTextanno.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
             /*   if (s?.length != 2) {
                    binding.editTextanno.setBackgroundResource(R.drawable.edittext_border_red)
                } else {
                    binding.editTextanno.setBackgroundResource(R.drawable.edittext_border_green)
                }*/
            }

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                if (input.length == 2) {
                    val anno = input.toIntOrNull()
                    if (anno != null) {
                        if (anno < 23) {
                            binding.editTextanno.setBackgroundResource(R.drawable.edittext_border_red)
                        }
                    }
                }
            }
        })

        binding.editTextnumero.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
              /*  if (s?.length != 16) {
                    binding.editTextnumero.setBackgroundResource(R.drawable.edittext_border_red)
                } else {
                    binding.editTextnumero.setBackgroundResource(R.drawable.edittext_border_green)
                }*/
            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length != 16) {
                    binding.editTextnumero.setBackgroundResource(R.drawable.edittext_border_red)
                }
            }
        })



        binding.buttonConfermaDati.setOnClickListener {
            if (!validateOtherFields(binding.editTextnumero,binding.editTextNomeCognome,binding.editTextCvv,binding.editTextmese,binding.editTextanno,)) {
                Toast.makeText(this, "Controllare il contenuto dei campi", Toast.LENGTH_SHORT).show()
            }
            else{
                //inserire il comportamento del bottone
            }
        }

    }
    private fun validateOtherFields(EditText1: EditText, EditText2: EditText, EditText3: EditText, EditText4: EditText, EditText5: EditText): Boolean {
        val field1 = EditText1.text.toString().trim()
        val field2 = EditText2.text.toString().trim()
        val field3 = EditText3.text.toString().trim()
        val field4 = EditText4.text.toString().trim()
        val field5 = EditText5.text.toString().trim()
        return field1.isNotEmpty() && field2.isNotEmpty() && field3.isNotEmpty()&& field4.isNotEmpty()&& field5.isNotEmpty()
    }
}