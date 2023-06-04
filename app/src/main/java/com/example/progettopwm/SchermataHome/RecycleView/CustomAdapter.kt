package com.example.progettopwm.SchermataHome.RecycleView

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopwm.R
import com.example.progettopwm.databinding.CardLocalitaBinding

class CustomAdapter(private val lista:List<ItemsViewModel>):RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    private var selectedIndex = -1

    private  var onclickListener: OnclickListener? = null
    class ViewHolder(binding:CardLocalitaBinding):RecyclerView.ViewHolder(binding.root){
        val icona = binding.icona
        val descrizione = binding.descrizione
        val linear = binding.cardLinear
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

        var previousHoleder:ViewHolder? = null
        val items = lista[position]
        holder.icona.text = getEmojiByUnicode(items.unicodeEmoji)
        holder.descrizione.text = items.descrizione
        holder.itemView.setOnClickListener{
            onclickListener?.onclick(position,items)
            if(selectedIndex == position){
                selectedIndex = -1
            }
            else{
                 val previousSelectedItem = selectedIndex
                selectedIndex = holder.adapterPosition
                if (previousSelectedItem != -1) {
                    notifyItemChanged(previousSelectedItem);
                }
            }
            notifyItemChanged(position);

        }
        if (selectedIndex == position) {
            // La card è selezionata
             // Cambia il colore come preferisci
            Log.i("Ciao","si")
            holder.linear.setBackgroundResource(R.drawable.card_localita_activated)
        } else {
            // La card non è selezionata
            Log.i("Ciao","no")
            holder.linear.setBackgroundResource(R.drawable.card_localita)
           // Cambia il colore come preferisci
        }



    }
    fun getEmojiByUnicode(unicode:Int):String{
        return  String(Character.toChars(unicode));
    }
    fun setOnClickListener(onClickListener: OnclickListener) {
        this.onclickListener = onClickListener
    }
    interface OnclickListener{
        fun onclick(position:Int, item:ItemsViewModel)
    }

}