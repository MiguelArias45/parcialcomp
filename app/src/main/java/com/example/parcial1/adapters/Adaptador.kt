package com.example.parcial1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.parcial1.R
import com.example.parcial1.database.Viaje

class Adaptador(private var viajes: List<Viaje>, private val onClick: (Viaje) -> Unit) :
    RecyclerView.Adapter<Adaptador.ViajeViewHolder>() {

    inner class ViajeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val destinoTextView: TextView = itemView.findViewById(R.id.text_view_destino)

        init {
            itemView.setOnClickListener {
                onClick(viajes[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViajeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_viaje, parent, false)
        return ViajeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViajeViewHolder, position: Int) {
        holder.destinoTextView.text = viajes[position].destino
    }

    override fun getItemCount(): Int = viajes.size

    fun updateViajes(newViajes: List<Viaje>) {
        viajes = newViajes
        notifyDataSetChanged()
    }
}
