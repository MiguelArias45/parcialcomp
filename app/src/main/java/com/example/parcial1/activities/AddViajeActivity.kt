package com.example.parcial1.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.parcial1.R
import com.example.parcial1.database.DatabaseHelper
import com.example.parcial1.database.Viaje

// Clase que representa la actividad para añadir o editar viajes
class AddViajeActivity : AppCompatActivity() {
    // Inicializa variables para manejar la base de datos y los campos de entrada
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var editTextDestino: EditText
    private lateinit var editTextFechaInicio: EditText
    private lateinit var editTextFechaFin: EditText
    private lateinit var editTextActividades: EditText
    private lateinit var editTextPresupuesto: EditText
    private var viajeId: Int? = null // Almacena el ID del viaje si se está editando

    // Método que se ejecuta al crear la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_viaje) // Establece el diseño de la actividad

        // Inicializa el objeto DatabaseHelper para manejar la base de datos
        databaseHelper = DatabaseHelper(this)

        // Inicializa los EditText para capturar la información del viaje
        editTextDestino = findViewById(R.id.edit_text_destino)
        editTextFechaInicio = findViewById(R.id.edit_text_fecha_inicio)
        editTextFechaFin = findViewById(R.id.edit_text_fecha_fin)
        editTextActividades = findViewById(R.id.edit_text_actividades)
        editTextPresupuesto = findViewById(R.id.edit_text_presupuesto)

        // Obtiene el ID del viaje desde el Intent, si existe
        viajeId = intent.getIntExtra("viaje_id", -1).takeIf { it != -1 }

        // Si hay un ID de viaje válido, carga los detalles del viaje
        if (viajeId != null && viajeId != -1) {
            loadViajeDetails(viajeId!!)
        }

        // Configura el botón para guardar o actualizar el viaje
        findViewById<Button>(R.id.button_save).setOnClickListener {
            // Si no hay ID de viaje, se guarda un nuevo viaje; de lo contrario, se actualiza
            if (viajeId == null || viajeId == -1) {
                saveViaje()
            } else {
                updateViaje()
            }
        }
    }

    // Método para cargar los detalles de un viaje en los campos de entrada
    private fun loadViajeDetails(id: Int) {
        // Obtiene el viaje de la base de datos utilizando el ID
        val viaje = databaseHelper.getAllViajes().first { it.id == id }
        // Rellena los campos de entrada con los detalles del viaje
        editTextDestino.setText(viaje.destino)
        editTextFechaInicio.setText(viaje.fechaInicio)
        editTextFechaFin.setText(viaje.fechaFin)
        editTextActividades.setText(viaje.actividades)
        editTextPresupuesto.setText(viaje.presupuesto.toString())
    }

    // Método para guardar un nuevo viaje en la base de datos
    private fun saveViaje() {
        // Crea un nuevo objeto Viaje con los datos ingresados
        val nuevoViaje = Viaje(
            destino = editTextDestino.text.toString(),
            fechaInicio = editTextFechaInicio.text.toString(),
            fechaFin = editTextFechaFin.text.toString(),
            actividades = editTextActividades.text.toString(),
            presupuesto = editTextPresupuesto.text.toString().toDoubleOrNull() ?: 0.0
        )
        // Agrega el nuevo viaje a la base de datos
        databaseHelper.addViaje(nuevoViaje)
        setResult(RESULT_OK) // Devuelve resultado OK
        finish() // Finaliza la actividad y regresa a la anterior
    }

    // Método para actualizar un viaje existente en la base de datos
    private fun updateViaje() {
        viajeId?.let { id -> // Utiliza el ID del viaje existente
            // Crea un objeto Viaje actualizado con los datos ingresados
            val updatedViaje = Viaje(
                id = id,
                destino = editTextDestino.text.toString(),
                fechaInicio = editTextFechaInicio.text.toString(),
                fechaFin = editTextFechaFin.text.toString(),
                actividades = editTextActividades.text.toString(),
                presupuesto = editTextPresupuesto.text.toString().toDoubleOrNull() ?: 0.0
            )
            // Actualiza el viaje en la base de datos
            databaseHelper.updateViaje(updatedViaje)
            setResult(RESULT_OK) // Devuelve resultado OK
            finish() // Finaliza la actividad y regresa a la anterior
        }
    }
}




