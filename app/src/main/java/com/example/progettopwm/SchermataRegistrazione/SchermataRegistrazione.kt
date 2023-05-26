package com.example.progettopwm.SchermataRegistrazione

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.progettopwm.R
import com.example.progettopwm.databinding.ActivitySchermataRegistrazioneBinding

class SchermataRegistrazione : AppCompatActivity() {
    private lateinit var binding: ActivitySchermataRegistrazioneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchermataRegistrazioneBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}