package com.example.progettopwm.SchermataHome.SchermataPreferiti.RecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopwm.SchermataHome.RecycleView.CustomAdapter
import com.example.progettopwm.databinding.FragmentPreferitiBinding

class CustomAdapterPreferiti(val lista:List<ItemViewModel>): RecyclerView.Adapter<CustomAdapterPreferiti.ViewHolder>(){
    class ViewHolder(val binding:FragmentPreferitiBinding):RecyclerView.ViewHolder(binding.root){
        val image = binding.immagineLocalita
        val titolo = binding.Titolo
        val testoLuogo = binding.testoLuogo
        val numeroPersone = binding.numeroPersone
        val prezzo = binding.costo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = FragmentPreferitiBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
       holder.image.setImageResource(item.image)
        holder.testoLuogo.text = item.luogo
        holder.titolo.text = item.titolo
        holder.numeroPersone.text = item.numPersone.toString()
        holder.prezzo.text = item.costo.toString()

    }

    override fun getItemCount(): Int {
        return lista.size
    }



}