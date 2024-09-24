package com.example.parcial1.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.parcial1.R
import com.example.parcial1.database.DatabaseHelper
import com.example.parcial1.database.Viaje

class AddViajeActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var editTextDestino: EditText
    private lateinit var editTextFechaInicio: EditText
    private lateinit var editTextFechaFin: EditText
    private lateinit var editTextActividades: EditText
    private lateinit var editTextPresupuesto: EditText
    private var viajeId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_viaje)

        databaseHelper = DatabaseHelper(this)

        editTextDestino = findViewById(R.id.edit_text_destino)
        editTextFechaInicio = findViewById(R.id.edit_text_fecha_inicio)
        editTextFechaFin = findViewById(R.id.edit_text_fecha_fin)
        editTextActividades = findViewById(R.id.edit_text_actividades)
        editTextPresupuesto = findViewById(R.id.edit_text_presupuesto)

        viajeId = intent.getIntExtra("viaje_id", -1).takeIf { it != -1 }

        if (viajeId != null && viajeId != -1) {
            loadViajeDetails(viajeId!!)
        }

        findViewById<Button>(R.id.button_save).setOnClickListener {
            if (viajeId == null || viajeId == -1) {
                saveViaje()
            } else {
                updateViaje()
            }
        }
    }

    private fun loadViajeDetails(id: Int) {
        val viaje = databaseHelper.getAllViajes().first { it.id == id }
        editTextDestino.setText(viaje.destino)
        editTextFechaInicio.setText(viaje.fechaInicio)
        editTextFechaFin.setText(viaje.fechaFin)
        editTextActividades.setText(viaje.actividades)
        editTextPresupuesto.setText(viaje.presupuesto.toString())
    }

    private fun saveViaje() {
        val nuevoViaje = Viaje(
            destino = editTextDestino.text.toString(),
            fechaInicio = editTextFechaInicio.text.toString(),
            fechaFin = editTextFechaFin.text.toString(),
            actividades = editTextActividades.text.toString(),
            presupuesto = editTextPresupuesto.text.toString().toDoubleOrNull() ?: 0.0
        )
        databaseHelper.addViaje(nuevoViaje)
        setResult(RESULT_OK) // Devolver resultado OK
        finish() // Regresar a la lista de viajes
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
            databaseHelper.updateViaje(updatedViaje)
            setResult(RESULT_OK) // Devolver resultado OK
            finish() // Regresar a la lista de viajes
        }
    }
}



