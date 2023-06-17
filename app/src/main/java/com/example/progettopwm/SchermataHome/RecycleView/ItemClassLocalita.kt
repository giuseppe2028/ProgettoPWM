package com.example.progettopwm.SchermataHome.RecycleView

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ItemClassLocalita(
    val id:Int,
    val image:Bitmap?,
    val title:String, val position:String, val rating:Double,
    val prezzo: String,
    val tipoViaggio:String,
    val numPersone:String,
    val continente:String
    ):Parcelable{
}
