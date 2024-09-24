package com.example.parcial1.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.parcial1.R
import com.example.parcial1.database.DatabaseHelper
import com.example.parcial1.database.Viaje

class ViajeDetailActivity : AppCompatActivity() {

    // Definimos las variables que usaremos para interactuar con la base de datos y las vistas
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var editTextDestino: EditText
    private lateinit var editTextFechaInicio: EditText
    private lateinit var editTextFechaFin: EditText
    private lateinit var editTextActividades: EditText
    private lateinit var editTextPresupuesto: EditText
    private lateinit var buttonDelete: Button
    private var viajeId: Int? = null // Variable para almacenar el ID del viaje a mostrar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_viaje) // Establece el layout correspondiente

        // Inicializamos la base de datos
        databaseHelper = DatabaseHelper(this)

        // Inicialización de las vistas (EditText y Button)
        editTextDestino = findViewById(R.id.edit_text_destino)
        editTextFechaInicio = findViewById(R.id.edit_text_fecha_inicio)
        editTextFechaFin = findViewById(R.id.edit_text_fecha_fin)
        editTextActividades = findViewById(R.id.edit_text_actividades)
        editTextPresupuesto = findViewById(R.id.edit_text_presupuesto)
        buttonDelete = findViewById(R.id.button_delete)

        // Obtención del ID del viaje desde el Intent que se pasó a esta actividad
        viajeId = intent.getIntExtra("viaje_id", -1).takeIf { it != -1 }

        // Si se recibió un ID de viaje válido, cargamos los detalles de ese viaje
        viajeId?.let {
            loadViajeDetails(it) // Llamamos a la función para cargar los detalles del viaje
        }

        // Configuración del botón "Actualizar"
        findViewById<Button>(R.id.button_update).setOnClickListener {
            updateViaje() // Cuando se hace clic, actualizamos los detalles del viaje
        }

        // Configuración del botón "Eliminar"
        buttonDelete.setOnClickListener {
            viajeId?.let { id -> deleteViaje(id) } // Si hay un ID de viaje, lo eliminamos
        }

        // Configuración del botón "Editar"
        findViewById<Button>(R.id.button_edit).setOnClickListener {
            viajeId?.let { id ->
                // Si se hace clic en "Editar", lanzamos la actividad de AddViajeActivity para modificar el viaje
                val intent = Intent(this, AddViajeActivity::class.java).apply {
                    putExtra("viaje_id", id) // Pasamos el ID del viaje a la actividad de edición
                }
                startActivity(intent) // Inicia la actividad de edición
            }
        }
    }

    // Función para cargar los detalles de un viaje según su ID
    private fun loadViajeDetails(id: Int) {
        val viaje = databaseHelper.getAllViajes().first { it.id == id }
        // Colocamos los detalles del viaje en los EditText correspondientes
        editTextDestino.setText(viaje.destino)
        editTextFechaInicio.setText(viaje.fechaInicio)
        editTextFechaFin.setText(viaje.fechaFin)
        editTextActividades.setText(viaje.actividades)
        editTextPresupuesto.setText(viaje.presupuesto.toString()) // Convertimos el presupuesto a texto
    }

    // Función para actualizar los detalles del viaje
    private fun updateViaje() {
        viajeId?.let { id ->
            // Creamos un objeto Viaje con los datos actuales del formulario
            val updatedViaje = Viaje(
                id = id, // Usamos el ID del viaje que estamos editando
                destino = editTextDestino.text.toString(), // Obtenemos el nuevo destino desde el EditText
                fechaInicio = editTextFechaInicio.text.toString(), // Nueva fecha de inicio
                fechaFin = editTextFechaFin.text.toString(), // Nueva fecha de fin
                actividades = editTextActividades.text.toString(), // Nuevas actividades
                presupuesto = editTextPresupuesto.text.toString().toDoubleOrNull() ?: 0.0 // Convertimos el presupuesto a Double, con un valor por defecto de 0.0
            )
            databaseHelper.updateViaje(updatedViaje) // Actualizamos el viaje en la base de datos
            finish() // Cerramos la actividad actual y regresamos a la lista de viajes
        }
    }

    // Función para eliminar un viaje según su ID
    private fun deleteViaje(id: Int) {
        databaseHelper.deleteViaje(id) // Eliminamos el viaje de la base de datos
        finish() // Cerramos la actividad actual y regresamos a la lista de viajes
    }
}
