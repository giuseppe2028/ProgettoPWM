package com.example.progettopwm.Recensioni

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.progettopwm.Gestione.idPersona
import com.example.progettopwm.GestioneDB
import com.example.progettopwm.Recensioni.RecyclerView.ItemViewModelRecensioni
import com.example.progettopwm.Recensioni.ScriviRecensione.RecyclerView.CustomAdapterScriviRecensione
import com.example.progettopwm.Recensioni.ScriviRecensione.RecyclerView.ItemViewModelRecensioniScrittura
import com.example.progettopwm.databinding.ActivityVisualizzaViaggioRecensioniBinding
import java.time.LocalDate

class VisualizzaViaggioRecensioni : AppCompatActivity() {
    private lateinit var binding:ActivityVisualizzaViaggioRecensioniBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVisualizzaViaggioRecensioniBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupSchermata()
    }

    private fun setupSchermata() {
        val lista:ArrayList<ItemViewModelRecensioniScrittura> = ArrayList()
        val data = LocalDate.now().toString()
        Log.i("data","$data")
        //faccio la query:
        //mi faccio dare la dimensione:
        //aggiungere immagine
        val query = "select count(id) as countId from  Compra C, Viaggio V where  C.ref_viaggio = V.id and C.ref_persona = 1 and data < '2023-06-15'"
        GestioneDB.richiestaInformazioni(query){
            dato->
            //count:
            val count = dato.get("countId").asInt

            for(i in 0..count){
                val query = "select V.id as id, luogo,nome_struttura,data,prezzo, recensione, num_persone, ref_immagine from Immagini I, Viaggio V, Compra C where C.ref_persona = ${idPersona.getId()} and V.data < $data and I.ref_viaggio = V.id and C.ref_viaggio = $i"
                //richiedo la card
                GestioneDB.queryImmagini(query){
                    dato,immagine->
                    lista.add(
                        ItemViewModelRecensioniScrittura(
                            dato.get("id").asInt,
                            immagine,
                            dato.get("nome_struttura").asString,
                            dato.get("luogo").asString,
                            dato.get("recensione").asDouble,
                            dato.get("data").asString,
                            dato.get("num_persone").asInt
                        )
                    )
                    Log.i("listasize", "${lista.size}")
                    CustomAdapterScriviRecensione(lista)
                }
            }
        }


    }


}