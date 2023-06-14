package com.example.progettopwm.SchermataIniziale

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.progettopwm.Gestione.ClientNetwork
import com.example.progettopwm.R
import com.example.progettopwm.databinding.FragmentPasswordDimenticataBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PasswordDimenticataFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PasswordDimenticataFragment : Fragment() {

    private lateinit var binding:FragmentPasswordDimenticataBinding
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPasswordDimenticataBinding.inflate(inflater)
        binding.CreaAccount.setOnClickListener {
            if (binding.emailRecuperaPassword.text.toString().trim().isEmpty()) {
                Toast.makeText(this.context, "Inserisci una mail", Toast.LENGTH_SHORT).show()
            } else {
                val email = binding.emailRecuperaPassword.text.toString().trim()
                recuperaPassword(email) { success, password ->
                    if (success) {
                        password?.let { notificaPassword(password) }
                    } else {
                        Toast.makeText(
                            context,
                            "Nessuna password associata all'email",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        // Inflate the layout for this fragment
        return binding.root
    }
    private fun notificaPassword(password:String) {
        val channelID:String = "ChannelID"
        //creo la notifica, ovvero creo il canale in cui inviare la notifica
        var channel: NotificationChannel = NotificationChannel(channelID,"MyChannel",
            NotificationManager.IMPORTANCE_DEFAULT )

        val notificationManager = activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        var builder = NotificationCompat.Builder(requireContext(), channelID)
            .setSmallIcon(R.drawable.gmail_icon__2020__svg)
            .setContentTitle("Password")
            .setContentText(password)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        notificationManager.notify(0,builder.build())
    }

    private fun recuperaPassword(mail: String, callback: (Boolean, String?) -> Unit){
        val query = "SELECT * FROM webmobile.Persona WHERE mail = '$mail'"
        ClientNetwork.retrofit.registrazione(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val resultSet = response.body()?.get("queryset") as JsonArray
                        if (resultSet.size() == 1) {
                            val password = resultSet[0].asJsonObject.get("password").asString
                            callback(true, password)
                        }else{
                            callback(false, null)
                        }
                    }
                    else{
                        callback(false, null)
                        Log.i("errore", "non funziona")
                        Log.i("errore", response.message())
                        Log.i("errore",  response.toString())

                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    callback(false, null)
                    Log.e("Errore", "Errore durante la chiamata di rete", t)
                    Toast.makeText(context, "Errore durante la chiamata di rete", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PasswordDimenticataFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PasswordDimenticataFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}