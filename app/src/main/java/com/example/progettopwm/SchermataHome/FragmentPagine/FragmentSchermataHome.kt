package com.example.progettopwm.SchermataHome.FragmentPagine

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
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
import com.example.progettopwm.databinding.CardLocalitaBinding
import com.example.progettopwm.databinding.FragmentSchermataHomeBinding

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
    private lateinit var listaLuogo:ArrayList<ItemClassLocalita>
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
        // Inflate the layout for this fragment
        return binding.root
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
            TODO("Qui andrà il codice di raffa")
            //transaction.replace(R.id.fragmentContainerHome,)
        }
    }

    private fun recycleViewGestore() {
         dati = arrayListOf<ItemsViewModel>(ItemsViewModel(127755,"Esplorazione"), ItemsViewModel(127958,"Mare"),ItemsViewModel(127957,"gita all'aria aperta"),ItemsViewModel(127956,"trekking"),ItemsViewModel(127963,"Cultura"))
         listaLuogo = arrayListOf<ItemClassLocalita>(ItemClassLocalita(R.drawable.foto,"Weekend in spiaggia","cefalu,Italy",4,"500$","Trekking"),
            ItemClassLocalita(R.drawable.foto,"Weekend in spiaggia","cefalu,Italy",4,"500$","Mare"),
            ItemClassLocalita(R.drawable.foto,"Weekend in spiaggia","cefalu,Italy",4,"500$","Esplorazione"),
            ItemClassLocalita(R.drawable.foto,"Weekend in spiaggia","cefalu,Italy",4,"500$","Trekking"),
            ItemClassLocalita(R.drawable.foto,"Weekend in spiaggia","cefalu,Italy",4,"500$","Cultura"),
            ItemClassLocalita(R.drawable.foto,"Weekend in spiaggia","cefalu,Italy",4,"500$","Mare"),
            ItemClassLocalita(R.drawable.foto,"Weekend in spiaggia","cefalu,Italy",4,"500$","Cultura"))
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
                startActivity(Intent(context,ActivitySchermataViaggio::class.java))
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
}