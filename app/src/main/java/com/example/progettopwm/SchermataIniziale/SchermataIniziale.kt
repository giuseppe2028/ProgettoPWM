package com.example.progettopwm.SchermataIniziale

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.progettopwm.databinding.ActivitySchermataInizialeBinding
import com.example.progettopwm.SchermataRegistrazione.SchermataRegistrazione

class SchermataIniziale : AppCompatActivity() {
    private lateinit var binding:ActivitySchermataInizialeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchermataInizialeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //clickBottoneLogin()
        clickBottoneRegistrazione()
    }


    private fun clickBottoneRegistrazione() {
        binding.bottoneRegistrzione.setOnClickListener {
            startActivity(Intent(this, SchermataRegistrazione::class.java))
        }
    }

    private fun clickBottoni() {
        binding.bottoneLogin.setOnClickListener {
          //  startActivity(Intent(this,Login::class.java))
        }
        }
    }


