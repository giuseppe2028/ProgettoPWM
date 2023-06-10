package com.example.progettopwm

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
        val idViaggio = 0
        super.onCreate(savedInstanceState)
        binding = SchermataViaggioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var stato:Boolean = false
        val id = intent.getIntExtra("idViaggio",0)

        setSchermata(id)
        binding.like.setOnClickListener {
            if(stato){
                binding.like.setImageDrawable(getDrawable(R.drawable.heartbutton))
                stato = false
            }else if(!stato){
                Log.i("ciao","prova3")
                binding.like.setImageDrawable(getDrawable(R.drawable.heart_3))
                stato = true
            }
        }

    }

    fun setSchermata(id:Int){
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