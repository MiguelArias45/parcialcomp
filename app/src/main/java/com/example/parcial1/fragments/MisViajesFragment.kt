package com.example.parcial1.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parcial1.R
import com.example.parcial1.activities.ViajeDetailActivity
import com.example.parcial1.adapters.Adaptador // Asegúrate de que el nombre sea correcto
import com.example.parcial1.database.DatabaseHelper
import com.example.parcial1.database.Viaje

class MisViajesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viajesAdapter: Adaptador // Cambia aquí a Adaptador
    private lateinit var databaseHelper: DatabaseHelper
    private var viajesList: List<Viaje> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout del fragmento
        return inflater.inflate(R.layout.fragment_mis_viajes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar el RecyclerView y el adaptador
        recyclerView = view.findViewById(R.id.recycler_view_viajes)
        databaseHelper = DatabaseHelper(requireContext())

        // Cargar viajes desde la base de datos
        viajesList = databaseHelper.getAllViajes() // Carga los viajes antes de inicializar el adaptador
        viajesAdapter = Adaptador(viajesList) { viaje ->
            // Manejar el clic en un viaje
            val intent = Intent(requireContext(), ViajeDetailActivity::class.java).apply {
                putExtra("viaje_id", viaje.id)
            }
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = viajesAdapter
    }
}
