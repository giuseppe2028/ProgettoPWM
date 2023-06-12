package com.example.progettopwm.Recensioni.RecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.progettopwm.SchermataHome.RecycleView.CustomAdapter
import com.example.progettopwm.databinding.CardLocalitaBinding
import com.example.progettopwm.databinding.CardRecensioneBinding

class CustomAdapterRecensioni(val lista:List<ItemViewModelRecensioni>):RecyclerView.Adapter<CustomAdapterRecensioni.ViewHolder>() {
    class ViewHolder(binding:CardRecensioneBinding):RecyclerView.ViewHolder(binding.root){
        val titolo = binding.titolo
        val rating  =binding.ratingBar4
        val testo = binding.testo
        val immagine = binding.imageProfile
        val utente  = binding.utente

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CardRecensioneBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        val item = lista[position]
        holder.testo.text = item.testo
        holder.utente.text = item.utente
        holder.rating.rating = item.rating.toFloat()
        //TODO(aggiungere immagine)
        holder.titolo.text = item.titolo

    }

}