package com.example.progettopwm.SchermataHome.SchermataPreferiti

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.progettopwm.Gestione.GestioneDB
import com.example.progettopwm.SchermataHome.SchermataPreferiti.RecyclerView.CustomAdapterPreferiti
import com.example.progettopwm.SchermataHome.SchermataPreferiti.RecyclerView.ItemViewModel
import com.example.progettopwm.databinding.FragmentSchermataPreferitiBinding
import com.google.gson.JsonObject

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SchermataPreferiti.newInstance] factory method to
 * create an instance of this fragment.
 */
class SchermataPreferiti : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding:FragmentSchermataPreferitiBinding

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
        binding = FragmentSchermataPreferitiBinding.inflate(inflater)
        binding.listaPreferiti.layoutManager = LinearLayoutManager(this.context)
        //faccio la query
        setSchermata()

        // Inflate the layout for this fragment
        return binding.root

    }



    private fun setSchermata() {
        //TODO(passare l'id della persona via intent)
        val query = "select distinct V.id,luogo,nome_struttura,recensione,prezzo,ref_immagine,num_persone from Viaggio V,Preferiti P,Immagini I where P.ref_viaggio = V.id and I.ref_viaggio = V.id"
        var iesimoDato: JsonObject  // Dichiarazione della variabile fuori dal callback
        var lista = ArrayList<ItemViewModel>()
//faccio la query
        GestioneDB.queryGenerica(query) { dati ->

            for (i in dati) {
                iesimoDato = i as JsonObject
                Log.i("entro", "entro")

                // Carico l'immagine in modo asincrono
                GestioneDB.getImage(i) { immagineRitorno ->
                    val immagine = immagineRitorno

                    // Aggiungo l'elemento alla lista
                     lista.add(
                        ItemViewModel(
                            iesimoDato.get("id").asInt,
                            immagine,
                            iesimoDato.get("nome_struttura").asString,
                            iesimoDato.get("luogo").asString,
                            iesimoDato.get("recensione").asDouble,
                            iesimoDato.get("prezzo").asDouble,
                            iesimoDato.get("num_persone").asInt
                        )
                    )

                    // Aggiorno l'adapter dopo l'aggiunta di un nuovo elemento
                    binding.listaPreferiti.adapter?.notifyDataSetChanged()
                }
            }
        }

// Imposto l'adapter fuori dal callback
       val adapter = CustomAdapterPreferiti(lista)
        binding.listaPreferiti.adapter = adapter
        adapter.setOnClickListener(object:
            CustomAdapterPreferiti.OnClickListener {
            override fun Onclick(position: Int, item: ItemViewModel) {
                val query = "delete from Preferiti where ref_viaggio = ${item.id}"
                GestioneDB.eliminaElemento(query)
                //lo aggiorno pure localemnte per evitare una query di troppo:
                lista.removeAt(position)
                adapter.notifyDataSetChanged()
            }
        }
        )

        //binding.listaPreferiti.adapter = CustomAdapterPreferiti(lista)





    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SchermataPreferiti.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SchermataPreferiti().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}