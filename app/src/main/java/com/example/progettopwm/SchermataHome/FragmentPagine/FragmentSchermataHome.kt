package com.example.progettopwm.SchermataHome.FragmentPagine

import android.annotation.SuppressLint
import android.app.AlertDialog
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
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.progettopwm.ActivitySchermataViaggio
import com.example.progettopwm.Gestione.ClientNetwork
import com.example.progettopwm.Gestione.idPersona
import com.example.progettopwm.GestioneDB
import com.example.progettopwm.R
import com.example.progettopwm.SchermataHome.FragmenCardProssimoViaggio.FragmentProssimoVIaggio
import com.example.progettopwm.SchermataHome.RecycleView.CustomAdapter
import com.example.progettopwm.SchermataHome.RecycleView.CustomAdapterMete
import com.example.progettopwm.SchermataHome.RecycleView.ItemClassLocalita
import com.example.progettopwm.SchermataHome.RecycleView.ItemsViewModel
import com.example.progettopwm.ViewDialog
import com.example.progettopwm.databinding.FragmentSchermataHomeBinding
import retrofit2.Callback
import com.google.gson.JsonArray
import com.google.gson.JsonObject
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
    private lateinit var adapter:CustomAdapter
    private lateinit var dati:ArrayList<ItemsViewModel>
    private var listaLuogo:ArrayList<ItemClassLocalita> = ArrayList()
    private lateinit var binding:FragmentSchermataHomeBinding
    private lateinit var adapterViaggi:CustomAdapterMete
    private var param1: String? = null
    private var param2: String? = null
    var idPerson:Int = 0


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
        val id_p= idPersona.getId()
        richiediImg(id_p){value, ref_immagine->
            if(value){
                if (ref_immagine != null) {
                    setUpImage(ref_immagine)
                }
            }
            else{
                Toast.makeText(context, "Errore durante la chiamata di rete", Toast.LENGTH_SHORT).show()
            }
        }
        popolaLista{
                updateList->
            listaLuogo = updateList
            adapterViaggi.filtraLista(listaLuogo)
            Log.i("ciao","${listaLuogo.size}")

        }
        filterButton()

        caricaViaggioProssimo(Date.valueOf(LocalDate.now().toString()))

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun filterButton() {

        binding.filterButton.setOnClickListener {
            val alert = ViewDialog()
            val ciao = alert.showDialog(activity) {
                    destinazione, numeroPersone,stato ->
                //devo filtrare la lista
                filtraListaDialog(destinazione, numeroPersone,stato)
            }
            val builder = AlertDialog.Builder(context)
            Log.i("ciao", "$ciao 123")
        }
    }

        /*val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.filtri, null)
        val alert = builder.create()
        alert.show()

         */

    private fun filtraListaDialog(destinazione: String, numeroPersone: String,ordineCrescente:Boolean) {

        val lista = if (numeroPersone == "Nessuno" && destinazione == "Tutte le destinazioni") {
            listaLuogo
        } else {
           listaLuogo.filter {
               it.continente == destinazione && it.numPersone == numeroPersone
           }
        }
        val listaOrdinata = if (ordineCrescente) {
            lista.sortedBy { it.prezzo }
        } else {
            lista.sortedByDescending { it.prezzo }
        }

        adapterViaggi.filtraLista(listaOrdinata)
        Log.i("debug1", "${lista.size}")
        /*if(ordine){
            Log.i("proa12","sonoqui")
            lista.sortBy {
                it.prezzo
            }
        }

         listaLuogo.filter {
                it.continente == destinazione && it.numPersone == numeroPersone
            } as ArrayList<ItemClassLocalita>


        else{
            lista.sortByDescending {
                it.prezzo
            }


        }
*/

    }


    private fun caricaViaggioProssimo(data: Date) {
        var id: Int
        val query =
            "select * from Compra,Viaggio where ref_viaggio = Viaggio.id and Compra.ref_persona = 1 and data>'$data'order by data"
        GestioneDB.richiestaInformazioni(query) { data ->
            //insersico la card
            val manager = childFragmentManager
            id = data.get("id").asInt
            childFragmentManager.setFragmentResult("request", Bundle().apply {
                putInt("id", id)
            })
            val transaction = manager.beginTransaction()
            val fragment = FragmentProssimoVIaggio()


            transaction.add(binding.frgmentProssimoViaggio.id, fragment)

            transaction.commit()
        }
    }


    private fun setProfilo() {
        val id = idPersona.getId()
        //TODO(ll'id sarà passato all'inizio del profilo)
        val query = "select nome,id from Persona where id = $id"
        GestioneDB.richiestaInformazioni(query){
            data ->
            //gestisco il JSON object
            val nome = data.get("nome").asString
            idPerson = data.get("id").asInt
            //sostituisco la stringa di default:
            val nomePrincipale = binding.nomeProfilo.text
            binding.nomeProfilo.text = nomePrincipale.replace("user".toRegex(), "$nome")


        }
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
                            binding.imageProfile.setImageBitmap(immagine)
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.i("ciao","fail")
                }

            }
        )


    }

    private fun richiediImg(id: Int, callback: (Boolean, String?) -> Unit){
        val query = "select ref_immagine from Persona where id =$id"
        ClientNetwork.retrofit.registrazione(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val resultSet = response.body()?.get("queryset") as JsonArray
                        if (resultSet.size() == 1) {
                            val ref_immagine= resultSet[0].asJsonObject.get("ref_immagine").asString
                            callback(true, ref_immagine)
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
         dati = arrayListOf<ItemsViewModel>(
             ItemsViewModel(127755,getString(R.string.Esplorazione)),
             ItemsViewModel(127958,getString(R.string.Mare)),
             ItemsViewModel(127957,getString(R.string.Relax)),
             ItemsViewModel(127956,getString(R.string.Trekking)),
             ItemsViewModel(127963,getString(R.string.Cultura)))


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
                intent.putExtra("Persona",idPerson)
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
        //seleziono la dimensione di queste count
        val query = "select count(Viaggio.id) as contatore from Viaggio, Immagini where  ref_viaggio = Viaggio.id and Immagini.immagine_default = 1"
        var datoRichiesto:Int
        GestioneDB.richiestaInformazioni(query){
            dato ->
            datoRichiesto = dato.get("contatore").asInt
            //setto ogni card:
            for(i in 0..datoRichiesto){
                val query = "select Viaggio.id as id, luogo, nome_struttura, recensione, prezzo, tipologia, ref_immagine,continente,Viaggio.num_persone from Viaggio, Immagini where  ref_viaggio = Viaggio.id and Immagini.immagine_default = 1 and Viaggio.id =$i"
                GestioneDB.queryImmagini(query){
                    elemento,immagine ->
                    lista.add(ItemClassLocalita(
                        elemento.get("id").asInt,
                        immagine,
                        elemento.get("nome_struttura").asString,
                        elemento.get("luogo").asString,
                        elemento.get("recensione").asDouble,
                        elemento.get("prezzo").asString.plus("$"),
                        elemento.get("tipologia").asString,
                        elemento.get("num_persone").asString,
                        elemento.get("continente").asString

                    )
                    )
                    //ordino la lista:
                   lista.sortByDescending {
                        it.rating
                    }
                    binding.frameLayout2.visibility = View.VISIBLE
                    callback(lista)
                }
            }

        }
        }


    private fun getImage(jsonObject: JsonObject,callback:(Bitmap?)->Unit){
        val string = jsonObject.get("ref_immagine").asString
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



