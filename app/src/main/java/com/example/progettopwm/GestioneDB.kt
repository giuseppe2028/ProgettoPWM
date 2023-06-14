package com.example.progettopwm


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import com.example.progettopwm.SchermataHome.RecycleView.ItemClassLocalita
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object GestioneDB {

    fun richiestaInformazioni(query:String,callback: (JsonObject)-> Unit){
        ClientNetwork.retrofit.registrazione(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val risposta = response.body()?.get("queryset") as JsonArray
                        if (risposta.size() != 0) {
                            Log.i("alpha", "ento")
                            //ho un solo oggetto JSON
                            callback(risposta.get(0) as JsonObject)
                        }
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("Problema","${t.message}")
                }

            }
        )
    }
    fun aggiornaServer(query: String){
        ClientNetwork.retrofit.update(query).enqueue(
            object: Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if(response.isSuccessful){
                        Log.i("Debug", "Query caricata con successo")
                    }
                    else{
                        Log.i("Debug", "Errore nella query")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("Debug", "Errore nel server")
                }

            }
        )
    }
    fun inserisciElemento(query: String){
        ClientNetwork.retrofit.insert(query).enqueue(
            object: Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if(response.isSuccessful){
                        Log.i("Debug", "Query caricata con successo")
                    }
                    else{
                        Log.i("Debug", "Errore nella query")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("Debug", "Errore nel server")
                }

            }
        )
    }
    fun eliminaElemento(query: String){

        ClientNetwork.retrofit.remove(query).enqueue(
            object: Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if(response.isSuccessful){
                        Log.i("Debug", "Query caricata con successo")
                    }
                    else{
                        Log.i("Debug", "Errore nella query")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("Debug", "Errore nel server")
                }

            }
        )
    }
    fun queryGenerica(query:String, callback: (JsonArray) -> Unit){
        ClientNetwork.retrofit.registrazione(query).enqueue(
            object: Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if(response.isSuccessful){
                        val risposta = response.body()?.get("queryset") as JsonArray
                        if(risposta.size() != 0){
                            callback(risposta)}
                        else{

                        }

                    }
                    else{
                        Log.i("Debug", "Errore nella query")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("Debug", "Errore nel server")
                }

            }
        )
    }
    fun getImage(jsonObject: JsonObject,callback:(Bitmap)->Unit){
        val string = jsonObject.get("ref_immagine").asString
        Log.i("ciao90", "$string")
        Log.i("ciaoProva","$string")
        ClientNetwork.retrofit.getImage(string).enqueue(
            object : Callback<ResponseBody>{
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if(response.isSuccessful){
                        var immagine: Bitmap? = null
                        if (response.body()!=null) {
                            immagine = BitmapFactory.decodeStream(response.body()?.byteStream())
                            callback(immagine)
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.i("ciao","fail")
                }

            }
        )

    }
    fun queryImmagini(query:String,callback:(JsonObject,Bitmap)->Unit){
        ClientNetwork.retrofit.registrazione(query).enqueue(
            object: Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if(response.isSuccessful){
                        val risposta = response.body()?.get("queryset") as JsonArray
                        if(risposta.size() != 0) {
                            //chiedo le immagini:
                            val elemento = risposta.get(0) as JsonObject
                            getImage(elemento){
                                immagine->
                                callback(elemento,immagine)
                            }

                        }
                        else{

                        }

                    }
                    else{
                        Log.i("Debug", "Errore nella query")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("Debug", "Errore nel server")
                }

            }
        )
    }
    fun login(query:String,context:Context,callback: (JsonObject)-> Unit){
        ClientNetwork.retrofit.registrazione(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val risposta = response.body()?.get("queryset") as JsonArray
                        if (risposta.size() != 0) {
                            Log.i("alpha", "ento")
                            //ho un solo oggetto JSON
                            callback(risposta.get(0) as JsonObject)
                        }
                        else if(risposta.size() == 0){
                            Toast.makeText(context,R.string.ToastLogin,Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("Problema","${t.message}")
                }

            }
        )
    }
}