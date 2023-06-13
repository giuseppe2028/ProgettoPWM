package com.example.progettopwm
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface InterfacciaAPI {

    @POST("postSelect/")
    @FormUrlEncoded
    fun registrazione(@Field("query") query: String): Call<JsonObject>
    @POST("postInsert/")
    @FormUrlEncoded
    fun insert(@Field("query") query: String): Call <JsonObject>
    @GET
    fun getImage(@Url url:String): Call <ResponseBody>
    @POST("postRemove/")
    @FormUrlEncoded
    fun remove(@Field("query") query: String)

    @POST("postUpdate/")
    @FormUrlEncoded
    fun update(@Field("query") query: String): Call <JsonObject>
}


