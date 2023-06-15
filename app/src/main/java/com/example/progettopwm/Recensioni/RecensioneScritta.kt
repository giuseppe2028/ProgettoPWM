package com.example.progettopwm.Recensioni

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.progettopwm.R
import com.example.progettopwm.databinding.ActivityScriviRecensioneBinding

class RecensioneScritta : AppCompatActivity() {
    private lateinit var binding:ActivityScriviRecensioneBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScriviRecensioneBinding.inflate(layoutInflater)
        setContentView(binding.root)
       val idViaggio = intent.getIntExtra("idViaggio",0)
        clickBottone()
    }

    private fun clickBottone() {
        val titolo = binding.insersciTitolo.text
        val testo = binding.inserisciTesto.text
        val rating = binding.ratingScrittura.rating
        Log.i("debuggg", "$titolo,$testo,$rating")
        //controllo i campi:
        binding.confermaButton.setOnClickListener {
            when{
                titolo.toString() == "" -> Toast.makeText(this,R.string.InserisciTitolo,Toast.LENGTH_SHORT).show()
                testo.toString() == "" ->Toast.makeText(this,R.string.InserisciTesto,Toast.LENGTH_SHORT).show()
                rating.toDouble() == 0.0 ->Toast.makeText(this,R.string.InserisciValuta,Toast.LENGTH_SHORT).show()
                else -> {
                    registraRisposta()
                }
            }
        }
    }

    private fun registraRisposta() {
        val query = "insert into Recensione values()"
    }
}