package com.example.progettopwm.SchermataHome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.progettopwm.R
import com.example.progettopwm.databinding.ActivitySchermataInserimentoDatiPagamentoBinding
import com.example.progettopwm.databinding.ActivitySchermataWalletBinding

class SchermataWallet : AppCompatActivity() {
    private lateinit var binding: ActivitySchermataWalletBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchermataWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonPaga.setOnClickListener{
            if (!validateOtherFields(binding.editText)) {
                Toast.makeText(this, "Controllare il contenuto dei campi", Toast.LENGTH_SHORT).show()
            }
            else{
                val inputText = binding.editText.text.toString().toInt()
                binding.textView9.text = inputText.toString()
            }
        }
    }

    private fun validateOtherFields(EditText1: EditText): Boolean {
        val field1 = EditText1.text.toString().trim()
        return field1.isNotEmpty()
    }
}
