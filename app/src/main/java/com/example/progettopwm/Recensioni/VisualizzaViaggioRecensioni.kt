package com.example.progettopwm.Recensioni

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.progettopwm.databinding.ActivityVisualizzaViaggioRecensioniBinding

class VisualizzaViaggioRecensioni : AppCompatActivity() {
    private lateinit var binding:ActivityVisualizzaViaggioRecensioniBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVisualizzaViaggioRecensioniBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}