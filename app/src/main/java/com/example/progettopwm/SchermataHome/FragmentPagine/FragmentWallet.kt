package com.example.progettopwm.SchermataHome.FragmentPagine

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import com.example.progettopwm.R
import com.example.progettopwm.SchermataHome.SchermataHome

import com.example.progettopwm.databinding.FragmentWalletBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentWallet.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentWallet : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentWalletBinding
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
    ): View? {
        var id_p = 0

        parentFragmentManager.setFragmentResultListener("requestKey", this) { requestKey, bundle ->
             id_p = bundle.getInt("id_p")
            // Utilizza l'id recuperato come desideri
            // Esegui le operazioni necessarie con l'id
        }
        binding.buttonPaga.setOnClickListener{
            if (!validateOtherFields(binding.editText)) {
                Toast.makeText(this.context, "Controllare il contenuto dei campi", Toast.LENGTH_SHORT).show()
            }
            else{
                var wallet = binding.editText.text.toString()
                aggiornaWallet(id_p, wallet.toInt())
                binding.editText.setText("")
                }
        }

        binding.buttonSaldo.setOnClickListener{
            recuperaWallet(id){ result, wallet->
                if(result){
                    binding.textView9.text = wallet.toString()
                }
                else{
                    binding.textView9.text = "null"
                }
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }
    private fun aggiornaWallet(id: Int, wallet: Int){
        val query = "UPDATE webmbile.Persona SET wallet = wallet + $wallet WHERE id = $id;"
        ClientNetwork.retrofit.update(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Errore durante la chiamata di rete", Toast.LENGTH_SHORT).show()
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

    private fun recuperaWallet(id: Int, callback: (Boolean, Int?) -> Unit){
        val query = "SELECT wallet FROM webmobile.Persona WHERE id = $id;"
        ClientNetwork.retrofit.registrazione(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val resultSet = response.body()?.get("queryset") as JsonArray
                        if (resultSet.size() == 1) {
                            val wallet = resultSet[0].asJsonObject.get("wallet").asInt
                            callback(true, wallet)
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
        // TODO: Rename and change types and number of parameters
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