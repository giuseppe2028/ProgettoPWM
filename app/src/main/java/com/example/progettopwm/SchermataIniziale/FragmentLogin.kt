package com.example.progettopwm.SchermataIniziale
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.progettopwm.ClientNetwork
import com.example.progettopwm.R
import com.example.progettopwm.SchermataHome.FragmentPagine.FragmentModificaDati
import com.example.progettopwm.SchermataHome.FragmentPagine.FragmentWallet
import com.example.progettopwm.SchermataHome.SchermataHome
import com.example.progettopwm.databinding.FragmentLoginBinding
import com.example.progettopwm.databinding.FragmentModificaDatiBinding
import com.example.progettopwm.idPersona
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
 * Use the [FragmentLogin.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentLogin : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding:FragmentLoginBinding
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

        sharedPreferences = requireActivity().getSharedPreferences("misvago", Context.MODE_PRIVATE)
        binding = FragmentLoginBinding.inflate(inflater)
        clickBottoni()

        // Inflate the layout for this fragment
        return binding.root
    }





    private fun verificaCredenziali(email: String, password: String, callback: (Boolean, Int?)->Unit){
        val query = "SELECT id FROM Persona WHERE mail = '$email' AND password ='$password'"
        ClientNetwork.retrofit.registrazione(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val resultSet = response.body()?.get("queryset") as JsonArray
                        if (resultSet.size() == 1) {
                            val id = resultSet[0].asJsonObject.get("id").asInt
                            callback(true, id)
                        }else{
                        callback(false, null)
                        }
                    }
                    else{
                        Log.i("errore", "non funziona")
                        Log.i("errore", response.message())
                        Log.i("errore",  response.toString())

                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    callback(false, null)
                }
            }
        )
    }
    private fun clickBottoni() {
        val result = true
        binding.passwordDimenticata.setOnClickListener{

          parentFragmentManager.setFragmentResult("requestKey", bundleOf("bundleKey" to result))
            }

        binding.accediConGoogle.setOnClickListener {
            parentFragmentManager.setFragmentResult("requestGoogle", bundleOf("RispostaGoogle" to result))
        }
        binding.accediNormale.setOnClickListener {
            if(binding.editTextText2.text.toString().trim().isEmpty() && binding.password.text.toString().trim().isEmpty()){
                Toast.makeText(this.context, R.string.ToastLogin, Toast.LENGTH_SHORT).show()
            }
            else{
                verificaCredenziali(binding.editTextText2.text.toString(), binding.password.text.toString()){ result, id_p->
                if(result){
                    if (id_p != null) {
                        //sistemo le preferences:
                        sharedPreferences.edit().putInt("id",id).apply()
                        idPersona.setId(id_p)
                    }
                    val intent = Intent(this.context,SchermataHome()::class.java)

                    startActivity(intent)

                }
                else{
                    Toast.makeText(this.context, R.string.ToastLogin, Toast.LENGTH_SHORT).show()

                }
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentLogin.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentLogin().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}