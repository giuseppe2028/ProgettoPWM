package com.example.progettopwm.SchermataHome.RecycleView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopwm.databinding.CardLocalitaBinding
import com.example.progettopwm.databinding.CardViaggiBinding

class CustomAdapterMete(private val lista:List<ItemClassLocalita>):RecyclerView.Adapter<CustomAdapterMete.ViewHolder>() {
    class ViewHolder(binding:CardViaggiBinding):RecyclerView.ViewHolder(binding.root){
        val imageView = binding.immagineLocalita
        val titolo = binding.titoloLocalita
        val rating = binding.ratingBar
        val prezzo = binding.prezzo
        val localita = binding.localita

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view that is used to hold list item
        val view = CardViaggiBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = lista[position]
        holder.imageView.setImageResource(items.image)
        holder.titolo.text = items.title
        holder.localita.text = items.position
        holder.rating.rating = items.rating.toFloat()
        holder.prezzo.text = items.prezzo

    }
    fun getEmojiByUnicode(unicode:Int):String{
        return  String(Character.toChars(unicode));
    }

}