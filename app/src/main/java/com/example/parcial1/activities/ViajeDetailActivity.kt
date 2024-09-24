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
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var editTextDestino: EditText
    private lateinit var editTextFechaInicio: EditText
    private lateinit var editTextFechaFin: EditText
    private lateinit var editTextActividades: EditText
    private lateinit var editTextPresupuesto: EditText
    private lateinit var buttonDelete: Button
    private var viajeId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_viaje)

        databaseHelper = DatabaseHelper(this)

        // Inicialización de los EditText y Button
        editTextDestino = findViewById(R.id.edit_text_destino)
        editTextFechaInicio = findViewById(R.id.edit_text_fecha_inicio)
        editTextFechaFin = findViewById(R.id.edit_text_fecha_fin)
        editTextActividades = findViewById(R.id.edit_text_actividades)
        editTextPresupuesto = findViewById(R.id.edit_text_presupuesto)
        buttonDelete = findViewById(R.id.button_delete)

        // Obtención del ID del viaje desde el Intent
        viajeId = intent.getIntExtra("viaje_id", -1).takeIf { it != -1 }

        viajeId?.let {
            loadViajeDetails(it) // Cargar detalles del viaje
        }

        findViewById<Button>(R.id.button_update).setOnClickListener {
            updateViaje() // Actualizar los detalles del viaje
        }

        buttonDelete.setOnClickListener {
            viajeId?.let { id -> deleteViaje(id) } // Eliminar el viaje
        }

        findViewById<Button>(R.id.button_edit).setOnClickListener {
            viajeId?.let { id ->
                val intent = Intent(this, AddViajeActivity::class.java).apply {
                    putExtra("viaje_id", id)
                }
                startActivity(intent) // Ir a la actividad para editar el viaje
            }
        }
    }

    private fun loadViajeDetails(id: Int) {
        val viaje = databaseHelper.getAllViajes().first { it.id == id }
        // Cargar los detalles del viaje en los EditText
        editTextDestino.setText(viaje.destino)
        editTextFechaInicio.setText(viaje.fechaInicio)
        editTextFechaFin.setText(viaje.fechaFin)
        editTextActividades.setText(viaje.actividades)
        editTextPresupuesto.setText(viaje.presupuesto.toString())
    }

    private fun updateViaje() {
        viajeId?.let { id ->
            val updatedViaje = Viaje(
                id = id,
                destino = editTextDestino.text.toString(),
                fechaInicio = editTextFechaInicio.text.toString(),
                fechaFin = editTextFechaFin.text.toString(),
                actividades = editTextActividades.text.toString(),
                presupuesto = editTextPresupuesto.text.toString().toDoubleOrNull() ?: 0.0
            )
            databaseHelper.updateViaje(updatedViaje) // Actualizar el viaje en la base de datos
            finish() // Regresar a la lista de viajes
        }
    }

    private fun deleteViaje(id: Int) {
        databaseHelper.deleteViaje(id) // Eliminar el viaje de la base de datos
        finish() // Regresar a la lista de viajes
    }
}
