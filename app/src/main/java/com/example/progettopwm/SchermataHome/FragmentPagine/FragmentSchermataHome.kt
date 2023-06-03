package com.example.progettopwm.SchermataHome.FragmentPagine

import ClientNetwork
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.progettopwm.ActivitySchermataViaggio
import com.example.progettopwm.R
import com.example.progettopwm.SchermataHome.RecycleView.CustomAdapter
import com.example.progettopwm.SchermataHome.RecycleView.CustomAdapterMete
import com.example.progettopwm.SchermataHome.RecycleView.ItemClassLocalita
import com.example.progettopwm.SchermataHome.RecycleView.ItemsViewModel
import com.example.progettopwm.databinding.ActivitySchermataHomeBinding
import com.example.progettopwm.databinding.FragmentSchermataHomeBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentSchermataHome.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentSchermataHome : Fragment() {
    private lateinit var binding:FragmentSchermataHomeBinding
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

        binding = FragmentSchermataHomeBinding.inflate(inflater)
        recycleViewGestore()
        clickProfile()
        setHome()
        // Inflate the layout for this fragment
        return binding.root
    }
    private fun setHome() {
        val query = "select nome, ref_immagine from Persona where cognome = 'Doe'"
        ClientNetwork.retrofit.registrazione(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        if ((response.body()?.get("queryset") as JsonArray).size() == 1) {

                            val nome =  ((response.body()?.get("queryset") as JsonArray).get(0) as JsonObject).get("nome")
                            val urlImage =  (response.body()?.get("queryset") as JsonArray).get(0) as JsonObject
                            binding.benvenuto.text = nome.toString()
                            Log.i("ciao","$urlImage")
                            getImage(urlImage)
                        } else {
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("Ciao", t.message.toString())
                    print(t.stackTrace)
                }
            }
        )

    }
    private fun getImage(jsonObject: JsonObject){
        val string = jsonObject.get("ref_immagine").asString
        Log.i("ciaoProva","$string")
        ClientNetwork.retrofit.getImage(string).enqueue(
            object : Callback<ResponseBody>{
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if(response.isSuccessful){
                        var avatar: Bitmap? = null
                        if (response.body()!=null) {
                            avatar = BitmapFactory.decodeStream(response.body()?.byteStream())
                        }
                        binding.imageProfile.setImageBitmap(avatar)
                        Log.i("ciao","successiful")
                    }
                    else{
                        Log.i("ciao","notsuccessiful")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.i("ciao","fail")
                }

            }
        )

    }

    private fun clickProfile() {
        binding.imageProfile.setOnClickListener {
            val manager = parentFragmentManager
            val transaction = manager.beginTransaction()
            TODO("Qui andrà il codice di raffa")
            //transaction.replace(R.id.fragmentContainerHome,)
        }
    }

    private fun recycleViewGestore() {
        val dati = arrayListOf<ItemsViewModel>(ItemsViewModel(127755,"Esplorazione"), ItemsViewModel(127958,"Mare"),ItemsViewModel(127957,"gita all'aria aperta"),ItemsViewModel(127956,"trekking"),ItemsViewModel(127963,"Cultura"))
        val listaLuogo = arrayListOf<ItemClassLocalita>(ItemClassLocalita(R.drawable.foto,"Weekend in spiaggia","cefalu,Italy",4,"500$"),
            ItemClassLocalita(R.drawable.foto,"Weekend in spiaggia","cefalu,Italy",4,"500$"),
            ItemClassLocalita(R.drawable.foto,"Weekend in spiaggia","cefalu,Italy",4,"500$"),
            ItemClassLocalita(R.drawable.foto,"Weekend in spiaggia","cefalu,Italy",4,"500$"),
            ItemClassLocalita(R.drawable.foto,"Weekend in spiaggia","cefalu,Italy",4,"500$"),
            ItemClassLocalita(R.drawable.foto,"Weekend in spiaggia","cefalu,Italy",4,"500$"),
            ItemClassLocalita(R.drawable.foto,"Weekend in spiaggia","cefalu,Italy",4,"500$"))
        val adapter = CustomAdapter(dati)
        val adapterViaggi = CustomAdapterMete(listaLuogo)

        binding.listaLocalita.layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)
        binding.listaLocalita.adapter = adapter
        binding.listaViaggi.layoutManager= LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)
        binding.listaViaggi.adapter = adapterViaggi

        //per ora hanno lo stesso adapter, poi esso va modificato
        binding.localitaSuggerite.layoutManager = GridLayoutManager(this.context,2)
        binding.localitaSuggerite.adapter = adapterViaggi
        adapterViaggi.setOnClickListener(object :
            CustomAdapterMete.OnClickListener{
            override fun Onclick(position: Int, item: ItemClassLocalita) {
                startActivity(Intent(context,ActivitySchermataViaggio::class.java))
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
         * @return A new instance of fragment FragmentSchermataHome.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentSchermataHome().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}