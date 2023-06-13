package com.example.progettopwm.SchermataHome.FragmenCardProssimoViaggio

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.progettopwm.Gestione.GestioneDB
import com.example.progettopwm.databinding.FragmentProssimoVIaggioBinding
import java.sql.Date
import java.time.LocalDate

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentProssimoVIaggio.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentProssimoVIaggio : Fragment() {
    private lateinit var binding:FragmentProssimoVIaggioBinding
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

        binding = FragmentProssimoVIaggioBinding.inflate(layoutInflater)
        mandaDatiFragment()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setSchermata(id:Int) {
        //TODO inserire l'id del viaggio
        var immagine:Bitmap
        Log.i("Viaggio","ciao1")
        val data = Date.valueOf(
            LocalDate.now().toString())
        val query = "select * from Compra,Viaggio,Immagini where Compra.ref_viaggio = Viaggio.id and Compra.ref_persona = 1 and data>'2023-06-12' and Viaggio.id = Immagini.ref_viaggio order by data "
        GestioneDB.richiestaInformazioni(query){

            dato ->

            binding.titoloProssimoViaggio.text= dato.get("nome_struttura").asString
            binding.dataProssimoViaggio.text = dato.get("data").asString
            binding.luogoProssimoViaggio.text = dato.get("luogo").asString
            GestioneDB.getImage(dato){
               dato ->
                binding.immagineViaggio.setImageBitmap(dato)
            }
        }
    }

    private fun mandaDatiFragment() {

        parentFragmentManager.setFragmentResultListener("request",this){
            requestKey,bundle->
            val risposta = bundle.getInt("id")

            //faccio la query
            setSchermata(id)
            //setto la schermata:

        }
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentProssimoVIaggio.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentProssimoVIaggio().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}