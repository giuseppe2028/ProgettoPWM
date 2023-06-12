package com.example.progettopwm.Recensioni.RecyclerView

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.progettopwm.SchermataHome.RecycleView.CustomAdapter

class CustomAdapterRecensioni(val lista:List<ItemViewModelRecensioni>):RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {

    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

}