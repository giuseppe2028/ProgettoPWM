package com.example.progettopwm.SchermataIniziale

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.progettopwm.Login.Login
import com.example.progettopwm.databinding.ActivitySchermataInizialeBinding

class SchermataIniziale : AppCompatActivity() {
    private lateinit var binding:ActivitySchermataInizialeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchermataInizialeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clickBottoni()
    }

    private fun clickBottoni() {
        binding.bottoneLogin.setOnClickListener {
            startActivity(Intent(this,Login::class.java))
        }
        }
    }


