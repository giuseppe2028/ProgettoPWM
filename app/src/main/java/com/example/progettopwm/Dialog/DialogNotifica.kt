package com.example.progettopwm.Dialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.appsearch.RemoveByDocumentIdRequest
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.progettopwm.Gestione.idPersona
import com.example.progettopwm.R
import com.example.progettopwm.SchermataHome.SchermataHome

class DialogNotifica : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.dialog_layout,
                view?.findViewById(R.id.constrain1)
            ))
                // Add action buttons
                .setPositiveButton(R.string.conferma,
                    DialogInterface.OnClickListener { dialog, id ->
                        // sign in the user ..
                        dialog.cancel()
                        startActivity(Intent(this.context,SchermataHome()::class.java))

                    })
            dialog?.window?.setLayout(400,400)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


}
