package com.example.progettopwm.SchermataHome.SchermataPrenotazioni.RecyclerView

import android.graphics.Bitmap

class ItemViewModelP(
    val id:Int,
    val image: Bitmap?, val dataA:String, val giornoPernotto:Int, val titolo: String,
    val luogo: String,
    val rating:Double, val costo:Double, val numPersone:Int) {

}