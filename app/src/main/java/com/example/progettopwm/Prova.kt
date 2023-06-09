package com.example.progettopwm

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object GestioneDB{
     fun setHome() {
        val query = "select nome, ref_immagine from Persona where cognome = 'Doe'"
        ClientNetwork.retrofit.registrazione(query)
    }

     fun getImage(jsonObject: JsonObject?, onResponse:(Bitmap) -> Unit){
        val string = jsonObject?.get("ref_immagine")?.asString
         if (string != null) {
             ClientNetwork.retrofit.getImage(string).enqueue(
                 object : Callback<ResponseBody>{
                     override fun onResponse(
                         call: Call<ResponseBody>,
                         response: Response<ResponseBody>
                     ) {
                         if(response.isSuccessful){
                             var avatar: Bitmap? = null
                             if (response.body()!=null) {
                                 avatar = BitmapFactory.decodeStream(response.body()?.byteStream())
                                 onResponse(avatar)
                             }

                         }
                         else{

                         }



                     }

                     override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                         TODO("Not yet implemented")
                     }

                 }
             )
         }


    }
}


/*fun  Call<ResponseBody>.prova(callback: (Call<ResponseBody>, Response<ResponseBody>) -> Unit){
    enqueue(
        object: Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                callback(call,response)
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                TODO("Not yet implemented")
            }

        }
    )

 */
