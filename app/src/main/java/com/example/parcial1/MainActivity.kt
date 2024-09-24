package com.example.parcial1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.parcial1.fragments.MisViajesFragment
import com.example.parcial1.activities.AddViajeActivity
import com.example.parcial1.activities.SearchViajeActivity // Asegúrate de importar esta actividad

class MainActivity : AppCompatActivity() {

    private val ADD_VIAJE_REQUEST_CODE = 1 // Añadir esta constante

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Cargar el fragmento de MisViajes
        loadFragment(MisViajesFragment())

        // Configurar el botón de añadir viaje
        val addViajeButton: Button = findViewById(R.id.button_add_viaje)
        addViajeButton.setOnClickListener {
            openAddViajeActivity()
        }

        // Configurar el botón de buscar viajes
        val searchViajeButton: Button = findViewById(R.id.button_search) // Asegúrate de que este ID exista en tu layout
        searchViajeButton.setOnClickListener {
            openSearchViajeActivity()
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun openAddViajeActivity() {
        val intent = Intent(this, AddViajeActivity::class.java)
        startActivityForResult(intent, ADD_VIAJE_REQUEST_CODE) // Cambia a startActivityForResult
    }

    private fun openSearchViajeActivity() {
        val intent = Intent(this, SearchViajeActivity::class.java)
        startActivity(intent) // Abrir la actividad de búsqueda de viajes
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_VIAJE_REQUEST_CODE && resultCode == RESULT_OK) {
            loadFragment(MisViajesFragment()) // Recargar el fragmento para mostrar los nuevos datos
        }
    }
}

