package com.example.progettopwm

import android.app.Activity
import android.app.Dialog
import android.util.Log
import android.view.Window
import com.example.progettopwm.databinding.FiltriBinding

class ViewDialog {
    private lateinit var binding:FiltriBinding
    fun showResetPasswordDialog(activity: Activity?) {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.filtri)
        binding = FiltriBinding.inflate(activity.layoutInflater)


        //val dialogBtn_remove = dialog.findViewById<TextView>(R.id.txtClose)
        // dialogBtn_remove.setOnClickListener {
        //dialog.dismiss()
        //activity!!.finish()

        dialog.show()
    }
}
