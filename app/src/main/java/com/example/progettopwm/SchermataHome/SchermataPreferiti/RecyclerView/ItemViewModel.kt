package com.example.progettopwm.SchermataHome.SchermataPreferiti.RecyclerView

import android.graphics.Bitmap

class ItemViewModel(
    val image: Bitmap?, val titolo:String,
    val luogo:String, val rating:Double, val costo:Double, val numPersone:Int) {
}