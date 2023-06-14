package com.example.progettopwm.Recensioni

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.progettopwm.GestioneDB
import com.example.progettopwm.R
import com.example.progettopwm.Recensioni.RecyclerView.ItemViewModelRecensioni
import com.example.progettopwm.databinding.ActivityViewRecensioniBinding

class ViewRecensioni : AppCompatActivity() {
    private lateinit var binding:ActivityViewRecensioniBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewRecensioniBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupSchermata()
    }

    private fun setupSchermata() {
        richiediInformazioni()
    }

    private fun richiediInformazioni() {
        val idViaggio = intent.getIntExtra("idViaggio",0)
        val lista:ArrayList<ItemViewModelRecensioni> = ArrayList()
        //faccio la query:
        //mi faccio dare la dimensione di tutte le recensioni:
        Log.i("viaggio","$idViaggio")
        val query ="select count(id) as id from Recensioni where ref_viaggio = $idViaggio"
        GestioneDB.richiestaInformazioni(query){
            count ->
            Log.i("count","$count")
            val dimensione = count.get("id").asInt
            if(dimensione != 0){
              //richiedo le altre:
                for(i in 1..dimensione){
                    //creo la singola card:
                    val query = "select nome,cognome,rating,titolo,testo from Recensioni,Persona where ref_utente = Persona.id and Viaggio.id = $idViaggio"
                    GestioneDB.richiestaInformazioni(query){
                        dato ->

                    }
                }
            }
        }
    }


}