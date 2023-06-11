package com.example.progettopwm.SchermataHome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.progettopwm.ActivitySchermataViaggio
import com.example.progettopwm.R
import com.example.progettopwm.SchermataHome.FragmentPagine.FragmentSchermataAccount
import com.example.progettopwm.SchermataHome.FragmentPagine.FragmentSchermataHome
import com.example.progettopwm.SchermataHome.SchermataPreferiti.SchermataPreferiti
import com.example.progettopwm.databinding.ActivitySchermataHomeBinding

class SchermataHome : AppCompatActivity() {
    private lateinit var binding:ActivitySchermataHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchermataHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        selezioneNavigationBar()
    }
    fun sostituisciFragment(fragment:Fragment):Boolean{
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
                else -> {
                     true
                }
            }

        }
    }


}