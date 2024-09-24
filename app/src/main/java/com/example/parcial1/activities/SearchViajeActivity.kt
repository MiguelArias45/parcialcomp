package com.example.parcial1.activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.parcial1.R
import com.example.parcial1.database.DatabaseHelper
import com.example.parcial1.database.Viaje // Asegúrate de tener este import
import java.io.BufferedReader
import java.io.InputStreamReader

class SearchViajeActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var textViewResultados: TextView
    private lateinit var editTextBusqueda: EditText // Campo de texto para búsqueda

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_viaje)

        databaseHelper = DatabaseHelper(this)
        textViewResultados = findViewById(R.id.text_view_resultados)
        editTextBusqueda = findViewById(R.id.edit_text_busqueda) // Inicializa el EditText

        // Configurar el botón de búsqueda
        val buttonBuscar: Button = findViewById(R.id.button_buscar)
        buttonBuscar.setOnClickListener {
            searchViajes()
        }
    }

    private fun searchViajes() {
        // Obtener el texto de búsqueda
        val query = editTextBusqueda.text.toString().trim()

        // Buscar viajes en el archivo según el texto ingresado
        val viajes = databaseHelper.searchViajesInFile("viajes.txt", query)

        // Crear una cadena con los detalles de los viajes
        val resultados = viajes.joinToString("\n") { viaje ->
            "Destino: ${viaje.destino}, Fecha Inicio: ${viaje.fechaInicio}, Fecha Fin: ${viaje.fechaFin}, Actividades: ${viaje.actividades}, Presupuesto: ${viaje.presupuesto}"
        }

        // Mostrar los resultados en el TextView
        textViewResultados.text = if (resultados.isNotEmpty()) {
            resultados
        } else {
            "No se encontraron viajes."
        }
    }

    private fun searchViajesInFile(fileName: String, searchTerm: String): List<Viaje> {
        val viajesEncontrados = mutableListOf<Viaje>()

        try {
            val fileInputStream = openFileInput(fileName) // Usa this, no context
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)

            bufferedReader.forEachLine { line ->
                val datos = line.split(",")
                if (datos.size == 6 && (datos[1].contains(searchTerm, true) || datos[4].contains(searchTerm, true))) {
                    val viaje = Viaje(
                        id = datos[0].toInt(),
                        destino = datos[1],
                        fechaInicio = datos[2],
                        fechaFin = datos[3],
                        actividades = datos[4],
                        presupuesto = datos[5].toDouble()
                    )
                    viajesEncontrados.add(viaje)
                }
            }

            bufferedReader.close()
        } catch (e: Exception) {
            Log.e("SearchViajeActivity", "Error al buscar viajes en archivo: ${e.message}")
        }

        return viajesEncontrados
    }
}

