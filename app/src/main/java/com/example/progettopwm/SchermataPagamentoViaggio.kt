package com.example.progettopwm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.progettopwm.Dialog.DialogNotifica
import com.example.progettopwm.Gestione.idPersona
import com.example.progettopwm.databinding.ActivitySchermataPagamentoViaggioBinding
import java.time.Duration

class SchermataPagamentoViaggio : AppCompatActivity() {
    var prezzo = 0
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
            prezzo = elemento.get("prezzo").asInt
            GestioneDB.getImage(elemento){
                    immagine->
                binding.immagineLocalita.setImageBitmap(immagine)
            }
            mostraDialog()
        }

    }

    private fun mostraDialog() {
        binding.comprami2.setOnClickListener {
            val dialog = DialogNotifica()
            dialog.show(supportFragmentManager,"conferma")
            if(!dialog.isVisible){
                //faccio la query
                controllaDati()
            }
        }
    }

    private fun controllaDati() {
        Log.i("saldo","prezzo: $prezzo")
        //faccio la query
        val query = "select saldo from Persona where id=${idPersona.getId()}"
        GestioneDB.richiestaInformazioni(query){
                saldoRichiesto ->
            val saldo = saldoRichiesto.get("saldo").asInt
            Log.i("saldo","$saldo")

            if(saldo>prezzo){
                aggiornaDati()
            }
            else if(saldo<prezzo){
                Log.i("sei qui","qui")
                Toast.makeText(this,"Metodo di pagamento respinto, non hai abbastaza soldi per effettuare il pagamento",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun aggiornaDati() {
        Log.i("ciao","$prezzo prezzooo")
        val query = "update Persona set saldo = saldo - $prezzo where id = ${idPersona.getId()}"
        GestioneDB.aggiornaServer(query)

    }
}