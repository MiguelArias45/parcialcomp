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
import com.example.parcial1.activities.ViajeDetailActivity // Importamos la actividad para mostrar detalles del viaje

// Adaptador para el RecyclerView que maneja una lista de objetos Viaje
class Adaptador(private val context: Context, private val viajes: List<Viaje>) : RecyclerView.Adapter<Adaptador.ViajeViewHolder>() {

    // Clase interna que representa el ViewHolder, que contendrá las vistas para cada ítem de la lista
    inner class ViajeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Definimos los elementos visuales que mostrarán los datos de cada viaje
        val textViewDestino: TextView = itemView.findViewById(R.id.text_view_destino)
        val textViewFechaInicio: TextView = itemView.findViewById(R.id.text_view_fecha_inicio)
        val textViewFechaFin: TextView = itemView.findViewById(R.id.text_view_fecha_fin)
        val textViewActividades: TextView = itemView.findViewById(R.id.text_view_actividades)
        val textViewPresupuesto: TextView = itemView.findViewById(R.id.text_view_presupuesto)

        // Inicializamos el ViewHolder con un listener para cuando se haga clic en un ítem de la lista
        init {
            // Configuramos el clic para que al hacer clic en un ítem se abra el detalle del viaje correspondiente
            itemView.setOnClickListener {
                val viaje = viajes[adapterPosition] // Obtenemos el viaje correspondiente a la posición clickeada
                // Creamos un Intent para abrir la actividad de detalles del viaje
                val intent = Intent(context, ViajeDetailActivity::class.java)
                intent.putExtra("viaje_id", viaje.id) // Pasamos el ID del viaje al Intent
                context.startActivity(intent) // Iniciamos la actividad de detalle del viaje
            }
        }
    }

    // Este método crea una nueva vista (ítem) para el RecyclerView cuando sea necesario
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViajeViewHolder {
        // Inflamos el layout del ítem de viaje y lo pasamos al ViewHolder
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_viaje, parent, false)
        return ViajeViewHolder(view) // Devolvemos un nuevo ViewHolder con la vista inflada
    }

    // Este método enlaza los datos de un viaje con las vistas correspondientes (por ejemplo, destino, fechas)
    override fun onBindViewHolder(holder: ViajeViewHolder, position: Int) {
        // Obtenemos el viaje en la posición actual
        val viaje = viajes[position]
        // Asignamos los datos del viaje a los TextViews correspondientes
        holder.textViewDestino.text = viaje.destino
        holder.textViewFechaInicio.text = viaje.fechaInicio
        holder.textViewFechaFin.text = viaje.fechaFin
        holder.textViewActividades.text = viaje.actividades
        holder.textViewPresupuesto.text = viaje.presupuesto.toString() // Convertimos el presupuesto a String
    }

    // Este método devuelve el número total de ítems en la lista (el tamaño de la lista de viajes)
    override fun getItemCount(): Int {
        return viajes.size // Devuelve el tamaño de la lista de viajes
    }
}





