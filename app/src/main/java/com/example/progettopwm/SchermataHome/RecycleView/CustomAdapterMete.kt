package com.example.progettopwm.SchermataHome.RecycleView

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.progettopwm.databinding.CardLocalitaBinding
import com.example.progettopwm.databinding.CardViaggiBinding

class CustomAdapterMete(private var lista:List<ItemClassLocalita>):RecyclerView.Adapter<CustomAdapterMete.ViewHolder>(){
    private var setOnClickListener:OnClickListener? = null
    class ViewHolder(binding:CardViaggiBinding):RecyclerView.ViewHolder(binding.root){
        val imageView = binding.immagineLocalita
        val titolo = binding.titoloLocalita
        val rating = binding.ratingBar
        val prezzo = binding.prezzo
        val localita = binding.localita

    }
    fun searchMete(list:List<ItemClassLocalita>){
        this.lista = list
        notifyDataSetChanged()
        Log.i("ciao","sono qui")
    }
interface OnClickListener{
    fun Onclick(position: Int,item:ItemClassLocalita)
}
    fun setOnClickListener(onClickListener: OnClickListener){
        this.setOnClickListener = onClickListener
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
    fun filtraLista(lista:List<ItemClassLocalita>){
        this.lista = lista
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val items = lista[position]
        holder.imageView.setImageBitmap(items.image)
        holder.titolo.text = items.title
        holder.localita.text = items.position
        holder.rating.rating = items.rating.toFloat()
        holder.prezzo.text = items.prezzo.toString()
        holder.itemView.setOnClickListener {
            setOnClickListener?.Onclick(position,items)
        }

    }

    fun getEmojiByUnicode(unicode:Int):String{
        return  String(Character.toChars(unicode));
    }



}