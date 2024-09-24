package com.example.parcial1.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.parcial1.R
import com.example.parcial1.database.Viaje
import com.example.parcial1.activities.ViajeDetailActivity // Asegúrate de importar la actividad correcta

class Adaptador(private val context: Context, private val viajes: List<Viaje>) : RecyclerView.Adapter<Adaptador.ViajeViewHolder>() {

    inner class ViajeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewDestino: TextView = itemView.findViewById(R.id.text_view_destino)
        val textViewFechaInicio: TextView = itemView.findViewById(R.id.text_view_fecha_inicio)
        val textViewFechaFin: TextView = itemView.findViewById(R.id.text_view_fecha_fin)
        val textViewActividades: TextView = itemView.findViewById(R.id.text_view_actividades)
        val textViewPresupuesto: TextView = itemView.findViewById(R.id.text_view_presupuesto)

        init {
            // Configurar el clic en el ítem
            itemView.setOnClickListener {
                val viaje = viajes[adapterPosition]
                val intent = Intent(context, ViajeDetailActivity::class.java) // Cambiar a ViajeDetailActivity
                intent.putExtra("viaje_id", viaje.id) // Pasa el ID del viaje
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViajeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_viaje, parent, false)
        return ViajeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViajeViewHolder, position: Int) {
        val viaje = viajes[position]
        holder.textViewDestino.text = viaje.destino
        holder.textViewFechaInicio.text = viaje.fechaInicio
        holder.textViewFechaFin.text = viaje.fechaFin
        holder.textViewActividades.text = viaje.actividades
        holder.textViewPresupuesto.text = viaje.presupuesto.toString()
    }

    override fun getItemCount(): Int {
        return viajes.size
    }
}





