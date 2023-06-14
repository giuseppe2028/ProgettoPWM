package com.example.progettopwm

import android.app.Activity
import android.app.Dialog
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import android.widget.Button
import android.widget.Spinner
import com.example.progettopwm.databinding.FiltriBinding

class ViewDialog {

    private lateinit var binding:FiltriBinding
    //TODO(aggiungere la data, e il prezzo)
    fun showDialog(activity: Activity?,callback:(String,String)->Unit) {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.filtri)
        val dialogBtn_remove = dialog.findViewById<Button>(R.id.bottoneConferma)

        dialogBtn_remove.setOnClickListener {
            //prendo i dati e li passo alla classe di sopra:
            val regione = dialog.findViewById<Spinner>(R.id.regione)
            val numPersoneView  = dialog.findViewById<Spinner>(R.id.numPersone)
            val destianazione = dialog.findViewById<Spinner>(R.id.regione).selectedItem.toString()
            val numPersone = numPersoneView.selectedItem.toString()
            val valore = regione.selectedItem.toString()

            Log.i("ciao","$destianazione")
            callback(destianazione,numPersone)
            dialog.dismiss()

        }



        //val dialogBtn_remove = dialog.findViewById<TextView>(R.id.txtClose)
        // dialogBtn_remove.setOnClickListener {
        //dialog.dismiss()
        //activity!!.finish()

        dialog.show()

    }
}
