package com.example.progettopwm.SchermataHome.RecycleView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopwm.databinding.CardLocalitaBinding

class CustomAdapter(private val lista:List<ItemsViewModel>):RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    private lateinit var onclickListener: OnclickListener
    class ViewHolder(binding:CardLocalitaBinding):RecyclerView.ViewHolder(binding.root){
        val icona = binding.icona
        val descrizione = binding.descrizione
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view that is used to hold list item
        val view = CardLocalitaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = lista[position]
        holder.icona.text = getEmojiByUnicode(items.unicodeEmoji)
        holder.descrizione.text = items.descrizione
        holder.itemView.setOnClickListener{
            onclickListener.onclick(position,items)
        }
    }
    fun getEmojiByUnicode(unicode:Int):String{
        return  String(Character.toChars(unicode));
    }
    fun setOnClickListener(onClickListener: OnclickListener) {
        this.onclickListener = onclickListener
    }
    interface OnclickListener{
        fun onclick(position:Int, item:ItemsViewModel)
    }

}