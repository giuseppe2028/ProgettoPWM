package com.example.progettopwm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.progettopwm.Dialog.DialogNotifica
import com.example.progettopwm.Gestione.GestioneDB
import com.example.progettopwm.databinding.ActivitySchermataPagamentoViaggioBinding

class SchermataPagamentoViaggio : AppCompatActivity() {
    private lateinit var binding:ActivitySchermataPagamentoViaggioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchermataPagamentoViaggioBinding.inflate(layoutInflater)
        setSchermata()
        setContentView(binding.root)

    }

    private fun setSchermata() {
        //ricevo l'id
        val id = intent.getIntExtra("idViaggio",0)
        //faccio la query:
        querySchermata(id)
    }

    private fun querySchermata(id:Int) {
        val query =
                "select nome_struttura,luogo,data,num_persone,prezzo,path_immagine from Viaggio, Immagini where Viaggio.id = $id and Immagini.ref_viaggio = Viaggio.id"
        GestioneDB.richiestaInformazioni(query){
            elemento ->
                binding.titoloViaggio.text = elemento.get("nome_struttura").asString
                binding.dataViaggio.text = elemento.get("data").asString
                binding.numeroPersone.text = elemento.get("num_persone").asString
                binding.testoLuogo.text = elemento.get("luogo").asString
                binding.prezzoViaggio2.text = elemento.get("prezzo").asString.plus("$")
                GestioneDB.getImage(elemento){
                    immagine->
                    binding.immagineLocalita.setImageBitmap(immagine)
                }
        }
        mostraDialog()
    }

    private fun mostraDialog() {
        binding.comprami2.setOnClickListener {
            val dialog = DialogNotifica()
            dialog.show(supportFragmentManager,"conferma")
        }
    }
}