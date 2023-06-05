package com.example.progettopwm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.graphics.drawable.toDrawable
import com.example.progettopwm.databinding.SchermataViaggioBinding


class ActivitySchermataViaggio : AppCompatActivity() {
    private lateinit var binding:SchermataViaggioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SchermataViaggioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var stato:Boolean = false
        binding.like.setOnClickListener {
            Log.i("ciao","prova1")
            if(stato){
                binding.like.setImageDrawable(getDrawable(R.drawable.heartbutton))
                stato = false
            }else if(!stato){
                Log.i("ciao","prova3")
                binding.like.setImageDrawable(getDrawable(R.drawable.heart_3))
                stato = true
            }
        }
    }
}