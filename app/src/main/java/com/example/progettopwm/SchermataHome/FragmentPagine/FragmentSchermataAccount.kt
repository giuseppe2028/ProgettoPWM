package com.example.progettopwm.SchermataHome.FragmentPagine

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import com.example.progettopwm.ClientNetwork
import com.example.progettopwm.R
import com.example.progettopwm.databinding.FragmentSchermataAccountBinding
import com.example.progettopwm.idPersona
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentSchermataAccount.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentSchermataAccount : Fragment() {
    private  lateinit var binding : FragmentSchermataAccountBinding
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
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSchermataAccountBinding.inflate(inflater)

        val result = true
        val id_p = idPersona.getId()
        richiediNome(id_p){value, nome, ref_immagine->
            if(value){
                binding.nome.text = nome
                if (ref_immagine != null) {
                    setUpImage(ref_immagine)
                }
            }
            else{
                Toast.makeText(context, "Errore durante la chiamata di rete", Toast.LENGTH_SHORT).show()
            }
        }
        // Inflate the layout for this fragment
        binding.textViewGestione.setOnClickListener{
            parentFragmentManager.setFragmentResult("requestMD", bundleOf("bundleMD" to result))

        }
        binding.textViewdatipagamento.setOnClickListener{
            parentFragmentManager.setFragmentResult("requestDP", bundleOf("bundleDP" to result))

        }
        binding.textViewwallet.setOnClickListener{
            parentFragmentManager.setFragmentResult("requestK", bundleOf("bundleK" to result))
        }
        return binding.root
    }

    private fun setUpImage(ref_immagine:String){
            ClientNetwork.retrofit.getImage(ref_immagine).enqueue(
                object : Callback<ResponseBody>{
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if(response.isSuccessful){
                            var immagine: Bitmap? = null
                            if (response.body()!=null) {
                                immagine = BitmapFactory.decodeStream(response.body()?.byteStream())
                                binding.circleImageView2.setImageBitmap(immagine)
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.i("ciao","fail")
                    }

                }
            )


    }

    private fun richiediNome(id: Int, callback: (Boolean, String?, String?) -> Unit){
        val query = "select nome, path_immagine from Persona where id =$id"
        ClientNetwork.retrofit.registrazione(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val resultSet = response.body()?.get("queryset") as JsonArray
                        if (resultSet.size() == 1) {
                            val nome= resultSet[0].asJsonObject.get("nome").asString
                            val ref_immagine= resultSet[0].asJsonObject.get("path_immagine").asString
                            callback(true, nome, ref_immagine)
                        }else{
                            callback(false, null, null)
                        }
                    }
                    else{
                        callback(false, null, null)
                        Log.i("errore", "non funziona")
                        Log.i("errore", response.message())
                        Log.i("errore",  response.toString())
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    callback(false, null, null)
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
         * @return A new instance of fragment FragmentSchermataAccount.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentSchermataAccount().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}