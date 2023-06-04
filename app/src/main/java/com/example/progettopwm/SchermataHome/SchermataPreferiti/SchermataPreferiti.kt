package com.example.progettopwm.SchermataHome.SchermataPreferiti

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.progettopwm.R
import com.example.progettopwm.SchermataHome.SchermataPreferiti.RecyclerView.CustomAdapterPreferiti
import com.example.progettopwm.SchermataHome.SchermataPreferiti.RecyclerView.ItemViewModel
import com.example.progettopwm.databinding.FragmentSchermataPreferitiBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SchermataPreferiti.newInstance] factory method to
 * create an instance of this fragment.
 */
class SchermataPreferiti : Fragment() {
    // TODO: Rename and change types of parameters
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
    ): View? {
        binding = FragmentSchermataPreferitiBinding.inflate(inflater)
        binding.listaPreferiti.layoutManager = LinearLayoutManager(this.context)
        binding.listaPreferiti.adapter = CustomAdapterPreferiti(listOf(
            ItemViewModel(R.drawable.foto,"Formentera", "italia",4.5,500.0,5),
            ItemViewModel(R.drawable.foto,"Formentera", "italia",4.5,500.0,5)
        ))
        // Inflate the layout for this fragment
        return binding.root

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
        // TODO: Rename and change types and number of parameters
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