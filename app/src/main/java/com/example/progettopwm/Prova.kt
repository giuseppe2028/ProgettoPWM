package com.example.progettopwm

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.progettopwm.SchermataHome.RecycleView.ItemClassLocalita
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object GestioneDB {

    fun richiestaInformazioni(query:String,callback: (JsonObject)-> Unit){
        val lista:ArrayList<ItemClassLocalita> = arrayListOf()
        ClientNetwork.retrofit.registrazione(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        Log.i("ciao", "ciao")
                        val risposta = response.body()?.get("queryset") as JsonArray
                        var immaginiCount = risposta.size()
                        if (risposta.size() == 1) {
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
}