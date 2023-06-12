package com.example.progettopwm

import android.app.Activity
import android.app.Dialog
import android.view.Window

class ViewDialog {
    fun showResetPasswordDialog(activity: Activity?) {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.filtri)

        //val dialogBtn_remove = dialog.findViewById<TextView>(R.id.txtClose)
        // dialogBtn_remove.setOnClickListener {
        //dialog.dismiss()
        //activity!!.finish()

        dialog.show()
    }
}
