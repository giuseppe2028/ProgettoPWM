package com.example.progettopwm.Recensioni.ScriviRecensione.RecyclerView

import android.graphics.Bitmap

class ItemViewModelPreferiti
    (
    val id:Int,
    val image: Bitmap?, val titolo:String,
    val luogo:String, val rating:Double, val data:String, val numPersone:Int) {
}