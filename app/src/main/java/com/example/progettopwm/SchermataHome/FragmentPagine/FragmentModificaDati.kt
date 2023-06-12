package com.example.progettopwm.SchermataHome.FragmentPagine

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.progettopwm.ClientNetwork
import com.example.progettopwm.databinding.FragmentModificaDatiBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentModificaDati.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentModificaDati : Fragment() {
    private lateinit var binding: FragmentModificaDatiBinding
    // TODO: Rename and change types of parameters
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
    ): View? {
        binding.buttonData.setOnClickListener {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            // Aggiorna il TextView con la data selezionata
            val selectedDate = "${selectedDay}/${selectedMonth + 1}/${selectedYear}"
            binding.textViewshowdata.text = selectedDate
        }, year, month, day)

        datePickerDialog.show()
    }
        // Inflate the layout for this fragment
        return binding.root
    }
    private fun recuperaDati(id: Int, callback: (Boolean, String?, String?, String?, String?, String?) -> Unit){
        val query = "SELECT nome, cognome, mail, data_nascita, password FROM webmobile.Persona WHERE id = $id;"
        ClientNetwork.retrofit.registrazione(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val resultSet = response.body()?.get("queryset") as JsonArray
                        if (resultSet.size() == 1) {
                            val nome = resultSet[0].asJsonObject.get("nome").asString
                            val cognome = resultSet[1].asJsonObject.get("cognome").asString
                            val mail = resultSet[2].asJsonObject.get("mail").asString
                            val data_nascita = resultSet[3].asJsonObject.get("data_nascita").asString
                            val password = resultSet[4].asJsonObject.get("password").asString
                            callback(true, nome, cognome, mail, data_nascita, password)
                        }else{
                            callback(false, null, null, null, null, null)
                        }
                    }
                    else{
                        callback(false, null, null, null, null, null)
                        Log.i("errore", "non funziona")
                        Log.i("errore", response.message())
                        Log.i("errore",  response.toString())
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    callback(false, null, null, null, null, null)
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
         * @return A new instance of fragment FragmentModificaDati.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentModificaDati().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}