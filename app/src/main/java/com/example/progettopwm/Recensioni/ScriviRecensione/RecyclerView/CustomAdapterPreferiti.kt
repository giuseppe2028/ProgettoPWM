package com.example.progettopwm.Recensioni.ScriviRecensione.RecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopwm.databinding.CardRecensioneBinding
import com.example.progettopwm.databinding.CardScriviRecensioneBinding
import com.example.progettopwm.databinding.FragmentPreferitiBinding

class CustomAdapterScriviRecensione(val lista:List<ItemViewModelPreferiti>): RecyclerView.Adapter<CustomAdapterScriviRecensione.ViewHolder>(){
    private var setOnClickListener:OnClickListener? = null
    class ViewHolder(val binding:CardScriviRecensioneBinding):RecyclerView.ViewHolder(binding.root){
        //val image = binding.immaginiLocalita
        val titolo = binding.Titolo
        val testoLuogo = binding.testoLuogo
        val recensione = binding.ratingBar2
        val numeroPersone = binding.numeroPersone
        val data = binding.costo
        val immagine = binding.immagineLocalita
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CardScriviRecensioneBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
       //holder.image.setImageResource(item.image)

        holder.immagine.setImageBitmap(item.image)
        holder.testoLuogo.text = item.luogo
        holder.titolo.text = item.titolo
        holder.recensione.rating = item.rating.toFloat()
        holder.numeroPersone.text = item.numPersone.toString()
        holder.data.text = item.data
    }


    override fun getItemCount(): Int {
        return lista.size
    }
    interface OnClickListener{
        fun Onclick(position: Int,item: ItemViewModelPreferiti)
    }
    fun setOnClickListener(onClickListener:OnClickListener){
        this.setOnClickListener = onClickListener
    }


}