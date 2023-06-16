package com.example.progettopwm.Login

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.example.progettopwm.Gestione.ClientNetwork
import com.example.progettopwm.R
import com.example.progettopwm.SchermataHome.SchermataHome
import com.example.progettopwm.SchermataRegistrazione.SchermataRegistrazione
import com.example.progettopwm.SchermataRegistrazione.datiUtente
import com.example.progettopwm.databinding.FragmentOTPBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var binding:FragmentOTPBinding

/**
 * A simple [Fragment] subclass.
 * Use the [OTPFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OTPFragment : Fragment() {


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
    ): View{

        val randomNumber = Random.nextInt(101, 901).toString()
        mostraNotifiche(randomNumber)
        binding = FragmentOTPBinding.inflate(inflater)
        // Inflate the layout for this fragment
        clickBottoni(randomNumber)
        return binding.root
    }

        private fun mostraNotifiche(rand:String) {
            val channelID:String = "ChannelID"
            //creo la notifica, ovvero creo il canale in cui inviare la notifica
            var channel: NotificationChannel = NotificationChannel(channelID,"MyChannel",
                NotificationManager.IMPORTANCE_DEFAULT )

            val notificationManager = activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            var builder = NotificationCompat.Builder(requireContext(), channelID)
                .setSmallIcon(R.drawable.gmail_icon__2020__svg)
                .setContentTitle("OTP")
                .setContentText(rand)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            notificationManager.notify(0,builder.build())
        }


    private fun clickBottoni(randomNumber: String) {
        binding.buttonOTP.setOnClickListener{
            if(binding.editTextOTP.text.toString().trim().isNotEmpty() && binding.editTextOTP.text.toString().equals(randomNumber)){
                setFragmentResultListener("requestKey"){
                        requestKey, bundle ->
                    val result = bundle.getParcelable<datiUtente>("bundleKey")
                    if (result != null) {
                        val nome = result.nome
                        val cognome = result.cognome
                        val email = result.email
                        val data = result.data
                        val password = result.password
                        caricaCredenziali(nome, cognome, email, data, password)
                    }
                }

                startActivity(Intent(this.context, SchermataHome()::class.java))

            }
            else{
                Toast.makeText(this.context, R.string.ToastOTP, Toast.LENGTH_SHORT).show()
                mostraNotifiche(randomNumber)
            }
        }
    }
    private fun caricaCredenziali(nome: String, cognome: String, email: String, data: String, password: String){
        val query = "INSERT INTO Persona (nome,  cognome, mail, data_nascita, password) VALUES ('$nome', '$cognome', '$email', '$data', '$password')"

        ClientNetwork.retrofit.insert(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        Log.i("ciao", response.body().toString())
                    }
                    else{
                        Log.i("errore", "non funziona")
                        Log.i("errore", response.message())
                        Log.i("errore",  response.toString())

                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("Errore", "Errore durante la chiamata di rete", t)
                    Toast.makeText(context, "Errore durante la chiamata di rete", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
    override fun onDestroyView() {
        super.onDestroyView()
        clearFragmentResultListener("requestKey")
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OTPFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OTPFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}