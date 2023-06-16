package com.example.progettopwm.Gestione

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.progettopwm.R

object FragmentUtil {
    fun refreshFragment(context:Context?) {
        context.let {
            val fragmentManager = (context as AppCompatActivity).supportFragmentManager
            fragmentManager.let {
                //cerco il fragment:
                val currentFragment = fragmentManager.findFragmentById(R.id.fragmentContainerHome)
                currentFragment.let {
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    if (it != null) {
                        fragmentTransaction.detach(it)
                        fragmentTransaction.attach(it)
                        fragmentTransaction.commit()
                    }

                }
            }
        }

    }
    }


