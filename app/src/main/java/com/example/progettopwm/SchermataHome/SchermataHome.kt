package com.example.progettopwm.SchermataHome

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.progettopwm.ActivitySchermataViaggio
import com.example.progettopwm.LanguageApp
import com.example.progettopwm.R
import com.example.progettopwm.SchermataHome.FragmentPagine.FragmentDatiPagamento
import com.example.progettopwm.SchermataHome.FragmentPagine.FragmentModificaDati
import com.example.progettopwm.SchermataHome.FragmentPagine.FragmentSchermataAccount
import com.example.progettopwm.SchermataHome.FragmentPagine.FragmentSchermataHome
import com.example.progettopwm.SchermataHome.FragmentPagine.FragmentWallet
import com.example.progettopwm.SchermataHome.SchermataPreferiti.SchermataPreferiti
import com.example.progettopwm.SchermataHome.SchermataPrenotazioni.SchermataPrenotazioni
import com.example.progettopwm.SchermataIniziale.FragmentLogin
import com.example.progettopwm.SchermataIniziale.PasswordDimenticataFragment
import com.example.progettopwm.databinding.ActivitySchermataHomeBinding
import java.util.Locale

class SchermataHome : AppCompatActivity() {
    private lateinit var binding:ActivitySchermataHomeBinding
    private var sentinellaCounter = 0
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = getSharedPreferences("misvago",Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        binding = ActivitySchermataHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selezioneNavigationBar()

        wallet()
        datiPagamento()
        modificaDati()
        selectLingua()

    }

    private fun selectLingua() {
        //attendo i dati del primo fragment
        supportFragmentManager.setFragmentResultListener("itemSelected",this){
                _, bundle ->

                val result = bundle.getString("itemSelectRisposta")
            Log.i("seleziona","$result")
            if(result!=null){
                if(result.equals("inglese")){
                    LanguageApp.setLocal(this,"en")
                    //inserisco la lingua nelle sharedPreferences
                    sharedPreferences.edit().putString("Lingua","en").apply()
                    finish()
                    startActivity(intent)
                }
                else if(result.equals("italiano")){
                    LanguageApp.setLocal(this,"it")
                    sharedPreferences.edit().putString("Lingua","it").apply()
                    finish()
                    startActivity(intent)
                }
            }

        }
    }

    private fun modificaDati() {

        supportFragmentManager
            .setFragmentResultListener("requestMD", this) { requestMD, bundle ->
                //devo inserire i fragment qua dentro per far ricominciare il contatore
                val manager = supportFragmentManager
                val trasaction = manager.beginTransaction()
                // We use a String here, but any type that can be put in a Bundle is supported.
                val result = bundle.getBoolean("bundleMD")
                if(result && sentinellaCounter == 0){
                    sentinellaCounter ++
                    trasaction.replace(binding.fragmentContainerHome.id, FragmentModificaDati()).addToBackStack(null).commit()
                }
                else{
                    trasaction.replace(binding.fragmentContainerHome.id, FragmentModificaDati()).addToBackStack(null).commit()
                }
            }
        fragmentListenerSignIn()
    }
    private fun datiPagamento() {

        supportFragmentManager
            .setFragmentResultListener("requestDP", this) { requestDP, bundle ->
                //devo inserire i fragment qua dentro per far ricominciare il contatore
                val manager = supportFragmentManager
                val trasaction = manager.beginTransaction()
                // We use a String here, but any type that can be put in a Bundle is supported.
                val result = bundle.getBoolean("bundleDP")
                if(result && sentinellaCounter == 0){
                    sentinellaCounter ++
                    trasaction.replace(binding.fragmentContainerHome.id, FragmentDatiPagamento()).addToBackStack(null).commit()
                }
                else{
                    trasaction.replace(binding.fragmentContainerHome.id, FragmentDatiPagamento()).addToBackStack(null).commit()
                }
            }
        fragmentListenerSignIn()
    }
    private fun wallet() {

        supportFragmentManager
            .setFragmentResultListener("requestK", this) { requestK, bundle ->
                //devo inserire i fragment qua dentro per far ricominciare il contatore
                val manager = supportFragmentManager
                val trasaction = manager.beginTransaction()
                // We use a String here, but any type that can be put in a Bundle is supported.
                val result = bundle.getBoolean("bundleK")
                if(result && sentinellaCounter == 0){
                    sentinellaCounter ++
                    trasaction.replace(binding.fragmentContainerHome.id, FragmentWallet()).addToBackStack(null).commit()

                    Log.i("prova","${manager.backStackEntryCount}")
                }
                else{
                    Log.i("prova","Faccio niente")
                    trasaction.replace(binding.fragmentContainerHome.id, FragmentWallet()).addToBackStack(null).commit()
                }
            }
        fragmentListenerSignIn()
    }
    private fun fragmentListenerSignIn() {
        supportFragmentManager
            .setFragmentResultListener("SignIn", this) { requestK, bundle ->
                //devo inserire i fragment qua dentro per far ricominciare il contatore
                val manager = supportFragmentManager
                val trasaction = manager.beginTransaction()
                // We use a String here, but any type that can be put in a Bundle is supported.
                val result = bundle.getBoolean("SignInRisposta")
                if(result){

                    trasaction.replace(binding.fragmentContainerHome.id, FragmentSchermataAccount()).addToBackStack(null).commit()

                    Log.i("prova","${manager.backStackEntryCount}")
                }
                else{
                    Log.i("prova","Faccio niente")
                }
            }
    }
    private fun sostituisciFragment(fragment:Fragment):Boolean{
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(binding.fragmentContainerHome.id,fragment).commit()
        return true
    }
    private fun selezioneNavigationBar() {
        binding.bottomNavigationView.setOnItemSelectedListener {item ->
            when(item.itemId){
                R.id.home -> sostituisciFragment(FragmentSchermataHome())
                R.id.preferiti -> sostituisciFragment(SchermataPreferiti())
               R.id.account -> sostituisciFragment(FragmentSchermataAccount())
                R.id.prenotazioni->sostituisciFragment(SchermataPrenotazioni())
                else -> {
                     true
                }
            }

        }
    }
    //blocco l'onpressed
    override fun onBackPressed() {

    }



}