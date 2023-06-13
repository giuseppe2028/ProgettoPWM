package com.example.progettopwm

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.example.progettopwm.Recensioni.GestioneSchermataViaggio.ContenitoreSchermataViaggio
import com.example.progettopwm.Recensioni.RecyclerView.recensioneFragment
import com.example.progettopwm.databinding.SchermataViaggioBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class ActivitySchermataViaggio : AppCompatActivity() {



    private lateinit var binding:SchermataViaggioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SchermataViaggioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //TODO da verificare
        if(savedInstanceState==null){
            val id = intent.getIntExtra("idViaggio",0)
            val idPersona = intent.getIntExtra("Persona",0)
            //carico il fragment
            val fragmentContenitoreViaggio = ContenitoreSchermataViaggio.newInstance(idPersona)
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.add(binding.fragmentContainerView2.id,fragmentContenitoreViaggio).commit()
            Log.i("id","$id")
        }


    }





}