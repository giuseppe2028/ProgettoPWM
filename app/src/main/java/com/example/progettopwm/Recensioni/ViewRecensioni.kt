package com.example.progettopwm.Recensioni

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.ProxyFileDescriptorCallback
import android.util.Log
import android.widget.Adapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.progettopwm.GestioneDB
import com.example.progettopwm.R
import com.example.progettopwm.Recensioni.RecyclerView.CustomAdapterRecensioni
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
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        richiediInformazioni(){
            dato->
            binding.recyclerView.adapter = CustomAdapterRecensioni(dato)
        }
    }

    private fun richiediInformazioni(callback: (ArrayList<ItemViewModelRecensioni>) ->Unit) {
        val idViaggio = intent.getIntExtra("idViaggio",0)
        val lista:ArrayList<ItemViewModelRecensioni> = ArrayList()
        //faccio la query:
        //mi faccio dare la dimensione di tutte le recensioni:
        Log.i("viaggio","$idViaggio")
        val query ="select count(id) as id from Recensioni where ref_viaggio = 1"
        GestioneDB.richiestaInformazioni(query){
            count ->
            Log.i("count","$count")
            val dimensione = count.get("id").asInt
            if(dimensione != 0){
              //richiedo le altre:
                for(i in 0..dimensione){
                    Log.i("listasize","$i")
                    //creo la singola card:
                    val query = "select nome,cognome,rating,titolo,testo,ref_immagine from Recensioni,Persona where ref_utente = Persona.id and ref_viaggio = $idViaggio and ref_utente = $i "
                    GestioneDB.queryImmagini(query){
                        dato,immagine->
                        //popolo la lista
                        lista.add(
                            ItemViewModelRecensioni(
                            dato.get("nome").asString + " " + dato.get("cognome").asString,
                                    dato.get("rating").asDouble,
                                    dato.get("titolo").asString,
                                    dato.get("testo").asString
                        )
                        )
                        Log.i("lista","${lista.size}")
                        callback(lista)
                    }

                }

            }
        }
    }


}