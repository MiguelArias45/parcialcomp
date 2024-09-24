package com.example.parcial1.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parcial1.R
import com.example.parcial1.database.DatabaseHelper
import com.example.parcial1.database.Viaje
import com.example.parcial1.adapters.Adaptador // Asegúrate de que el adaptador está importado correctamente

class MisViajesFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viajeAdapter: Adaptador
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_mis_viajes, container, false)
        recyclerView = view.findViewById(R.id.recycler_view_viajes) // Asegúrate de que el ID coincide
        databaseHelper = DatabaseHelper(requireContext())

        loadViajes() // Cargar los viajes
        return view
    }

    private fun loadViajes() {
        val viajes: List<Viaje> = databaseHelper.getAllViajes()
        viajeAdapter = Adaptador(requireContext(), viajes) // Pasar el contexto y la lista de viajes
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = viajeAdapter
    }
}


