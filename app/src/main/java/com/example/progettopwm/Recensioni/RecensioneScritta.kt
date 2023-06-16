package com.example.progettopwm.Recensioni

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import com.example.progettopwm.Gestione.idPersona
import com.example.progettopwm.GestioneDB
import com.example.progettopwm.R
import com.example.progettopwm.SchermataHome.SchermataHome
import com.example.progettopwm.databinding.ActivityScriviRecensioneBinding
import javax.security.auth.callback.Callback

class RecensioneScritta : AppCompatActivity() {
    private  var  idViaggio:Int = 0
    private lateinit var binding:ActivityScriviRecensioneBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScriviRecensioneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        idViaggio = intent.getIntExtra("idViaggio",0)
        clickBottone()
    }

    private fun clickBottone() {

        val titolo = binding.inserisciTitolo.text
        val testo = binding.insersciTesto.text



        //controllo i campi:
        binding.confermaButton.setOnClickListener {
            var rating:Float = binding.ratingScrittura.rating
            Log.i("debuggg", "$titolo,$testo,$rating")
            when{
                titolo.toString() == "" -> Toast.makeText(this,R.string.InserisciTitolo,Toast.LENGTH_SHORT).show()
                testo.toString() == "" ->Toast.makeText(this,R.string.InserisciTesto,Toast.LENGTH_SHORT).show()
                //rating.toDouble() == 0.0 ->Toast.makeText(this,R.string.InserisciValuta,Toast.LENGTH_SHORT).show()
                else -> {
                    registraRisposta(titolo.toString(),testo.toString(),rating.toString())
                }
            }
        }
    }

    private fun registraRisposta(titolo:String, testo: String,rating:String) {
        Log.i("viaggioBello","$idViaggio,${idPersona.getId()},$rating,$titolo,$testo")
        val query = "insert into Recensioni(ref_viaggio,ref_utente,rating,titolo,testo) values($idViaggio,${idPersona.getId()},$rating,'$titolo','$testo')"
        GestioneDB.inserisciElemento(query)
        Toast.makeText(this,R.string.PopupRecensione,Toast.LENGTH_SHORT).show()
        val i = Intent(this,SchermataHome::class.java)
        startActivity(i)

    }
}