package com.example.progettopwm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.progettopwm.databinding.SchermataViaggioBinding

class ActivitySchermataViaggio : AppCompatActivity() {
    private lateinit var binding:SchermataViaggioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SchermataViaggioBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}