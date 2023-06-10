package com.example.progettopwm

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.progettopwm.SchermataHome.FragmentPagine.FragmentSchermataHome
import com.example.progettopwm.SchermataHome.RecycleView.ItemClassLocalita
import com.example.progettopwm.SchermataHome.SchermataHome
import com.example.progettopwm.databinding.SchermataViaggioBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActivitySchermataViaggio : AppCompatActivity(),SchermataHome.DatiPassati {


    private lateinit var binding:SchermataViaggioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SchermataViaggioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var stato:Boolean = false
        val id = intent.getIntExtra("idViaggio",0)
        val idPersona = intent.getIntExtra("Persona",0)
        setSchermata(id)
        Log.i("id","$id")
        //setto il cuore a seconda del risultato della query:
        setLike(id)
        binding.like.setOnClickListener {
            if(stato){
                //disattivo il bottone:
                Toast.makeText(this,R.string.Disattivato, Toast.LENGTH_SHORT).show()
                binding.like.setImageDrawable(getDrawable(R.drawable.heartbutton))
                stato = false
                //request http
                aggiornaDati(idPersona,id,false)
            }else if(!stato){
                Toast.makeText(this,R.string.Attivato,Toast.LENGTH_SHORT).show()
                binding.like.setImageDrawable(getDrawable(R.drawable.heart_3))
                stato = true
                aggiornaDati(idPersona,id,true)
            }
        }

    }
    private fun setLike(id:Int){
        val query = "select * from Preferiti where ref_viaggio = $id"
        GestioneDB.queryGenerica(query){
            data->
            if(data.size()!=0){
                //vuol dire che l'elemento è nei preferiti
                binding.like.setImageDrawable(getDrawable(R.drawable.heart_3))
            }
            else if(data.size() ==0){
                binding.like.setImageDrawable(getDrawable(R.drawable.heartbutton))
            }
        }
    }
    fun aggiornaDati(idPersona:Int,id:Int,attivazione:Boolean) {
    //aggiungo o rimuovo
        if(attivazione){
            Log.i("debug","debug: Persona: $idPersona, Viaggio:$id")
            val query = "insert into Preferiti(ref_persona,ref_viaggio) values($idPersona,$id)"
            GestioneDB.inserisciElemento(query)
        }else if(!attivazione){
            val query = "delete from Preferiti where ref_viaggio = $id"
            GestioneDB.eliminaElemento(query)
        }
    }
    fun setSchermata(id:Int){
        //TODO(Selezionare il cuore su rosso o nero a secondo della query)
        Log.i("Ciao","$id")
        val query = "select * from Viaggio, Immagini where  ref_viaggio = Viaggio.id and Viaggio.id = $id and immagine_default = 1"
        ClientNetwork.retrofit.registrazione(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if(response.isSuccessful){
                        val risposta = response.body()?.get("queryset") as JsonArray
                        if(risposta.size() != 0){
                            Log.i("ciao123wdq","${(risposta.get(0) as JsonObject).get("nome_struttura").asString}")
                            binding.titleViaggio.text = (risposta.get(0) as JsonObject).get("nome_struttura").asString
                            binding.numeroPersone.text = (risposta.get(0) as JsonObject).get("num_persone").asString
                            binding.testoLuogo.text = (risposta.get(0) as JsonObject).get("luogo").asString
                            binding.ratingBar3.rating = (risposta.get(0) as JsonObject).get("recensione").asFloat
                            binding.testoDescrizione.text = (risposta.get(0) as JsonObject).get("descrizione").asString
                            binding.prezzoViaggio.text = (risposta.get(0) as JsonObject).get("prezzo").asString.plus("$")
                            val image = risposta.get(0) as JsonObject
                            //mi prendo l'immagine:
                            getImage(image)
                            binding.sfondo.background

                        }
                        else{
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("prova","prova123")
                }

            }
        )
    }
    private fun getImage(jsonObject: JsonObject){
        val string = jsonObject.get("path_immagine").asString
        Log.i("ciao90", "$string")
        Log.i("ciaoProva","$string")
        ClientNetwork.retrofit.getImage(string).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if(response.isSuccessful){
                        var immagine: Bitmap? = null
                        if (response.body()!=null) {
                            immagine = BitmapFactory.decodeStream(response.body()?.byteStream())
                            binding.sfondo.setImageBitmap(immagine)
                        }
                    }
                    else{
                        Log.i("ciao","notsuccessiful")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.i("ciao","fail")
                }

            }
        )


    }

    override fun datiPassati(value: Int) {
        Log.i("ciao","$value")
    }


}