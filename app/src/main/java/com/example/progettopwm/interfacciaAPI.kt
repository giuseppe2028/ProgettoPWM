package com.example.progettopwm

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface interfacciaAPI {

    @POST("postSelect/")
    @FormUrlEncoded
    fun registrazione(@Field("query") query: String): Call<JsonObject>



}