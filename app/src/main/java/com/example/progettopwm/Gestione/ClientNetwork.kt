package com.example.progettopwm.Gestione

import com.example.progettopwm.Interfacce.InterfacciaAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ClientNetwork {

    val intercector = HttpLoggingInterceptor()
    val client = OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS).addInterceptor(
        intercector
    ).build()
    val retrofit by lazy{  Retrofit.Builder().baseUrl("http://10.0.2.2:8000/webmobile/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client).build().create(InterfacciaAPI::class.java)}


}