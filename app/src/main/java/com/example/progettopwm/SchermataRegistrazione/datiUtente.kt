package com.example.progettopwm.SchermataRegistrazione

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class datiUtente(
    val nome: String,
    val cognome: String,
    val email: String,
    val data: String,
    val password: String
    ):Parcelable {
}
