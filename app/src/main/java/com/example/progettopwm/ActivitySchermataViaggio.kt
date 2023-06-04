package com.example.progettopwm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.graphics.drawable.toDrawable
import com.example.progettopwm.databinding.SchermataViaggioBinding


class ActivitySchermataViaggio : AppCompatActivity() {
    private lateinit var binding:SchermataViaggioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SchermataViaggioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.like.setOnClickListener {
            if(binding.like.isEnabled){
                binding.like.setImageDrawable(getDrawable(R.drawable.heartbutton))
            }else{
                binding.like.setImageDrawable(getDrawable(R.drawable.heart_3))
            }
        }
    }
}