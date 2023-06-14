package com.example.progettopwm.SchermataHome.SchermataPreferiti.RecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopwm.databinding.FragmentPreferitiBinding

class CustomAdapterPreferiti(val lista:List<ItemViewModel>): RecyclerView.Adapter<CustomAdapterPreferiti.ViewHolder>(){
    private var setOnClickListener:OnClickListener? = null
    class ViewHolder(val binding:FragmentPreferitiBinding):RecyclerView.ViewHolder(binding.root){
        //val image = binding.immaginiLocalita
        val titolo = binding.Titolo
        val testoLuogo = binding.testoLuogo
        val recensione = binding.ratingBar2
        val numeroPersone = binding.numeroPersone
        val prezzo = binding.costo
        val immagine = binding.immagineLocalita
        val elimina = binding.elimina
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = FragmentPreferitiBinding.inflate(LayoutInflater.from(parent.context))
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
        holder.prezzo.text = item.costo.toString().plus("$")
        holder.elimina.setOnClickListener {
            setOnClickListener?.Onclick(position,item)
        }
    }


    override fun getItemCount(): Int {
        return lista.size
    }
    interface OnClickListener{
        fun Onclick(position: Int,item: ItemViewModel)
    }
    fun setOnClickListener(onClickListener:OnClickListener){
        this.setOnClickListener = onClickListener
    }


}