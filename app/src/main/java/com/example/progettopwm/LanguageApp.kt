package com.example.progettopwm

import android.app.Activity
import java.util.Locale

object LanguageApp {
    fun setLocal(activity: Activity, langCode:String){
        val locale: Locale = Locale(langCode)
        Locale.setDefault(locale)
        val resources = activity.resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config,resources.displayMetrics)


    }

}