package com.example.progettopwm.SchermataHome.SchermataPrenotazioni.RecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopwm.databinding.FragmentPrenotazioniBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CustomAdapterPrenotazioni(val listaPre:List<ItemViewModelP>): RecyclerView.Adapter<CustomAdapterPrenotazioni.ViewHolder>(){
    private var setOnClickListener:OnClickListener? = null
    class ViewHolder(val binding:FragmentPrenotazioniBinding):RecyclerView.ViewHolder(binding.root){
        //val image = binding.immaginiLocalita
        val titolo = binding.Titolo
        val testoLuogo = binding.testoLuogo
        val numeroPersone = binding.numeroPersone
        val prezzo = binding.costo
        val immagine = binding.immagineLocalita
        val elimina = binding.elimina
        val scarica = binding.download
        val dataA= binding.testoArrivo
        val dataP=binding.testoPartenza
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = FragmentPrenotazioniBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listaPre[position]
       //holder.image.setImageResource(item.image)

        holder.immagine.setImageBitmap(item.image)
        holder.testoLuogo.text = item.luogo.toString()
        holder.dataP.text = calcolaData(item.dataA, item.giornoPernotto)
        holder.dataA.text = item.dataA
        holder.titolo.text = item.titolo
        holder.numeroPersone.text = item.numPersone.toString()
        holder.prezzo.text = item.costo.toString().plus("$")
        holder.elimina.setOnClickListener {
            setOnClickListener?.Onclick(position,item)
        }
        holder.scarica.setOnClickListener {
            setOnClickListener?.onScaricaClick(position,item)
        }


    }
private fun calcolaData(dataI: String, giorni:Int): String{
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(dataI, formatter)

    // Aggiunta dei giorni alla data
    val nuovaData = date.plusDays(giorni.toLong())

    // Formattazione della nuova data nel formato "yyyy-MM-dd"
    val nuovaDataStringa = nuovaData.format(formatter)

    return nuovaDataStringa

}

    override fun getItemCount(): Int {
        return listaPre.size
    }
    interface OnClickListener{
        fun Onclick(position: Int,item: ItemViewModelP)
        fun onScaricaClick(position: Int, item: ItemViewModelP)
    }

    fun setOnClickListener(onClickListener:OnClickListener){
        this.setOnClickListener = onClickListener
    }


}