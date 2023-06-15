package com.example.progettopwm.SchermataHome.FragmentPagine

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.progettopwm.Gestione.ClientNetwork
import com.example.progettopwm.Gestione.idPersona

import com.example.progettopwm.databinding.FragmentWalletBinding
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
 * Use the [FragmentWallet.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentWallet : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentWalletBinding
   // private val idPLiveData: MutableLiveData<Int> = MutableLiveData()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


binding = FragmentWalletBinding.inflate(inflater)



        binding.buttonPaga.setOnClickListener{
            if (!validateOtherFields(binding.editText)) {
                Toast.makeText(this.context, "Controllare il contenuto dei campi", Toast.LENGTH_SHORT).show()
            }
            else{
                val saldo = binding.editText.text.toString()
                val id_p= idPersona.getId()
                aggiornaWallet(id_p, saldo.toDouble())
                binding.editText.setText("")
            }
        }

        val id_p=idPersona.getId()
        recuperaWallet(id_p){ result, saldo->
            if(result){
                binding.textView9.text = saldo.toString()
            } else{
                binding.textView9.text = "null"
            }
        }


        // Inflate the layout for this fragment
        return binding.root
    }


    private fun aggiornaWallet(id: Int, saldo: Double){
        val query = "UPDATE Persona SET saldo = saldo + $saldo WHERE id = $id;"
        ClientNetwork.retrofit.update(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
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

    private fun recuperaWallet(id: Int, callback: (Boolean, Double?) -> Unit){
        val query = "SELECT saldo FROM Persona WHERE id = $id"
        ClientNetwork.retrofit.registrazione(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val resultSet = response.body()?.get("queryset") as JsonArray
                        if (resultSet.size() == 1) {
                            val saldo = resultSet[0].asJsonObject.get("saldo").asDouble
                            callback(true, saldo)
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


    private fun validateOtherFields(EditText1: EditText): Boolean {
        val field1 = EditText1.text.toString().trim()
        return field1.isNotEmpty()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentWallet.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentWallet().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }


    }
}