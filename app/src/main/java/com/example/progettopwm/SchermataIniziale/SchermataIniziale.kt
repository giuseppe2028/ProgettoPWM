package com.example.progettopwm.SchermataIniziale

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.progettopwm.Login.Login
import com.example.progettopwm.databinding.ActivitySchermataInizialeBinding

class SchermataIniziale : AppCompatActivity() {
    private lateinit var binding:ActivitySchermataInizialeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivitySchermataInizialeBinding.inflate(layoutInflater)
            setContentView(binding.root)


    }




}


