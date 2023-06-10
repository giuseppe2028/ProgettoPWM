package com.example.progettopwm.SchermataHome.RecycleView

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//TODO per ora le immagini le mettiamo in locale
@Parcelize
class ItemClassLocalita(
    val id:Int,
    val image:Bitmap?,
    val title:String, val position:String, val rating:Double,
    val prezzo: String,
    val tipoViaggio:String):Parcelable{
}