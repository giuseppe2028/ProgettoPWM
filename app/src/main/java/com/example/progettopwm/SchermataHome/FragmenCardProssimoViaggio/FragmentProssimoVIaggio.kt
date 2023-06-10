package com.example.progettopwm.SchermataHome.FragmenCardProssimoViaggio

import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.progettopwm.R
import com.example.progettopwm.databinding.FragmentProssimoVIaggioBinding

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
        setSchermata()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setSchermata() {
        val query = ""
    }

    private fun mandaDatiFragment() {
        Log.i("Viaggio","prova risposta")
        parentFragmentManager.setFragmentResultListener("request",this){
            requestKey,bundle->
            val risposta = bundle.getString(requestKey)

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