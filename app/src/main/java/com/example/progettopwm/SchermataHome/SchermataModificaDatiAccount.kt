package com.example.progettopwm.SchermataHome

import android.app.Activity
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.progettopwm.R
import com.example.progettopwm.databinding.ActivitySchermataModificaDatiAccountBinding
import com.example.progettopwm.databinding.ActivitySchermataRegistrazioneBinding
import java.util.Calendar

class SchermataModificaDatiAccount : AppCompatActivity() {
    private lateinit var binding: ActivitySchermataModificaDatiAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchermataModificaDatiAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonData.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                // Aggiorna il TextView con la data selezionata
                val selectedDate = "${selectedDay}/${selectedMonth + 1}/${selectedYear}"
                binding.textViewshowdata.text = selectedDate
            }, year, month, day)

            datePickerDialog.show()
        }

    }
}