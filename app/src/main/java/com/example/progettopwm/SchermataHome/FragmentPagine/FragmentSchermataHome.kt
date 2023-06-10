package com.example.progettopwm.SchermataHome.FragmentPagine

import ClientNetwork
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.progettopwm.ActivitySchermataViaggio
import com.example.progettopwm.GestioneDB
import com.example.progettopwm.SchermataHome.FragmenCardProssimoViaggio.FragmentProssimoVIaggio
import com.example.progettopwm.SchermataHome.RecycleView.CustomAdapter
import com.example.progettopwm.SchermataHome.RecycleView.CustomAdapterMete
import com.example.progettopwm.SchermataHome.RecycleView.ItemClassLocalita
import com.example.progettopwm.SchermataHome.RecycleView.ItemsViewModel
import com.example.progettopwm.SchermataHome.SchermataHome
import com.example.progettopwm.databinding.FragmentSchermataHomeBinding
import com.example.progettopwm.interfacciaAPI
import retrofit2.Callback
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.sql.Date
import java.time.LocalDate


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentSchermataHome.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentSchermataHome : Fragment() {
    private lateinit var datiPassati:SchermataHome.DatiPassati
    private lateinit var adapter:CustomAdapter
    private lateinit var dati:ArrayList<ItemsViewModel>
    private var listaLuogo:ArrayList<ItemClassLocalita> = ArrayList()
    private lateinit var binding:FragmentSchermataHomeBinding
    private lateinit var adapterViaggi:CustomAdapterMete
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
        filtraLista()
        gestioneSearchView()
        setProfilo()
        popolaLista{
                updateList->
            listaLuogo = updateList
            adapterViaggi.filtraLista(listaLuogo)
            Log.i("ciao","${listaLuogo.size}")

        }

        caricaViaggioProssimo(Date.valueOf(LocalDate.now().toString()))

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun caricaViaggioProssimo(data: Date) {
        var id: Int
        val query =
            "select * from Compra,Viaggio where ref_viaggio = Viaggio.id and Compra.ref_persona = 1 and data>'$data'order by data"
        GestioneDB.richiestaInformazioni(query) { data ->
            //insersico la card
            val manager = parentFragmentManager
            val transaction = manager.beginTransaction()
            val fragment = FragmentProssimoVIaggio()
            transaction.add(binding.frgmentProssimoViaggio.id, fragment).commit()
            id = data.get("id").asInt
            childFragmentManager.setFragmentResult("request", bundleOf("chiaveRisposta" to id))
            Log.i("value", "$id")
        }
    }


    private fun setProfilo() {
        //TODO(ll'id sarà passato all'inizio del profilo)
        val query = "select nome from Persona where id = 1"
        GestioneDB.richiestaInformazioni(query){
            data ->
            //gestisco il JSON object
            val nome = data.get("nome").asString
            //sostituisco la stringa di default:
            val nomePrincipale = binding.nomeProfilo.text
            binding.nomeProfilo.text = nomePrincipale.replace("user".toRegex(), "$nome")


        }
    }


    private fun filtraLista() {

        adapter.setOnClickListener(
            object : CustomAdapter.OnclickListener{
                override fun onclick(position: Int, item: ItemsViewModel) {
                    filtaListaByMete(item.descrizione)
                }

            }
        )


    }

    private fun filtaListaByMete(stringa:String) {
        val lista:ArrayList<ItemClassLocalita> = ArrayList()
        //filtro la lista
        for(i in listaLuogo){
            if(i.tipoViaggio.contains(stringa)){
                lista.add(i)
            }
        }
        adapterViaggi.filtraLista(lista)

    }

    private fun clickProfile() {
        binding.imageProfile.setOnClickListener {
            val manager = parentFragmentManager
            val transaction = manager.beginTransaction()
            //transaction.replace(,FragmentSchermataAccount())
        }
    }

    private fun recycleViewGestore() {
         dati = arrayListOf<ItemsViewModel>(ItemsViewModel(127755,"Esplorazione"), ItemsViewModel(127958,"Mare"),ItemsViewModel(127957,"gita all'aria aperta"),ItemsViewModel(127956,"trekking"),ItemsViewModel(127963,"Cultura"))


         adapter = CustomAdapter(dati)
         adapterViaggi = CustomAdapterMete(listaLuogo)

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
                val intent = Intent(activity,ActivitySchermataViaggio()::class.java)
                intent.putExtra("idViaggio", item.id)
                startActivity(intent)
            }
            }
        )
    }
    private fun gestioneSearchView() {
        binding.searchView.clearFocus()
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(
            object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    listaFiltrata(newText)
                    return true
                }

            }
        )
    }
    private fun listaFiltrata(newText: String?) {
        val listaFiltrata = ArrayList<ItemClassLocalita>()
        if( newText!=null){
            //cerco il valore
            for(i in listaLuogo){
                if( i.title.lowercase().contains(newText)){
                    listaFiltrata.add(i)
                }
            }
            adapterViaggi.searchMete(listaFiltrata)

        }
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
    fun popolaLista(callback: (ArrayList<ItemClassLocalita>) -> Unit){
        Log.i("popola","sono in popola lista")
        val lista:ArrayList<ItemClassLocalita> = arrayListOf()
        //val query = "select luogo, nome_struttura, recensione,prezzo, tipologia from Viaggio"
        val query = "select Viaggio.id as id, luogo, nome_struttura, recensione, prezzo, tipologia, path_immagine from Viaggio, Immagini where  ref_viaggio = Viaggio.id and Immagini.immagine_default = 1"
        ClientNetwork.retrofit.registrazione(query).enqueue(
         object : Callback<JsonObject> {
             @SuppressLint("SuspiciousIndentation")
             override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                 if(response.isSuccessful){
                     Log.i("ciao","DentroSono")
                     val risposta = response.body()?.get("queryset") as JsonArray
                     var immaginiCount = risposta.size()
                     if(risposta.size() != 0){
                         Log.i("ciao","DentroSonoQua")
                         for(i in risposta){
                             val jsonObjectElemento = i as JsonObject
                                getImage(jsonObjectElemento){
                                    immagine ->
                                    Log.i("Debug","${jsonObjectElemento.get("id").asInt} localita: ${jsonObjectElemento.get("nome_struttura").asString}" )
                                    lista.add(ItemClassLocalita(
                                        jsonObjectElemento.get("id").asInt,immagine,jsonObjectElemento.get("nome_struttura").asString,
                                        jsonObjectElemento.get("luogo").asString, jsonObjectElemento.get("recensione").asDouble,
                                        jsonObjectElemento.get("prezzo").asString.plus("$"), jsonObjectElemento.get("tipologia").asString)
                                    )

                              immaginiCount--
                            if(immaginiCount == 0){
                                callback(lista)
                            }
                                }
                         }
                     }else{
                         Log.i("Errore","${response.errorBody()}")
                         Log.i("Errore","Errore")}
                 }else{
                     Log.i("Errore","${response.errorBody()}")
                     Log.i("Errore","Errore")
                 }
             }

             override fun onFailure(call: Call<JsonObject>, t: Throwable) {

             }

         }
        )
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is SchermataHome.DatiPassati){
            datiPassati = context
        }
    }
    private fun getImage(jsonObject: JsonObject,callback:(Bitmap?)->Unit){
        val string = jsonObject.get("path_immagine").asString
        Log.i("ciao90", "$string")
        Log.i("ciaoProva","$string")
        ClientNetwork.retrofit.getImage(string).enqueue(
            object : Callback<ResponseBody>{
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if(response.isSuccessful){
                        var immagine: Bitmap? = null
                        if (response.body()!=null) {
                            immagine = BitmapFactory.decodeStream(response.body()?.byteStream())
                            callback(immagine)
                        }
                        callback(immagine)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.i("ciao","fail")
                }

            }
        )

    }

}