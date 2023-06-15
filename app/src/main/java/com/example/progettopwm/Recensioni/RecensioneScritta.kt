package com.example.progettopwm.Recensioni

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.progettopwm.R
import com.example.progettopwm.databinding.ActivityScriviRecensioneBinding

class RecensioneScritta : AppCompatActivity() {
    private lateinit var binding:ActivityScriviRecensioneBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScriviRecensioneBinding.inflate(layoutInflater)
        setContentView(binding.root)
       val idViaggio = intent.getIntExtra("idViaggio",0)

    }
}