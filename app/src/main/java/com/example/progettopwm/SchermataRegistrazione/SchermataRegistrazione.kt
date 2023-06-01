package com.example.progettopwm.SchermataRegistrazione

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import com.example.progettopwm.R
import com.example.progettopwm.databinding.ActivitySchermataRegistrazioneBinding

class SchermataRegistrazione : AppCompatActivity() {
    private lateinit var binding: ActivitySchermataRegistrazioneBinding
    private var isPasswordVisible = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchermataRegistrazioneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null){
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.add(R.id.fragmentContainerView, FragmentRegistrazione())
            transaction.commit()
        }


    }




}