package com.example.parcial1.activities

// Importación de clases necesarias para el funcionamiento de la actividad
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.parcial1.R
import com.example.parcial1.database.DatabaseHelper
import com.example.parcial1.database.Viaje
import java.io.BufferedReader
import java.io.InputStreamReader

// Definición de la actividad SearchViajeActivity, que extiende AppCompatActivity para heredar sus funcionalidades
class SearchViajeActivity : AppCompatActivity() {
    // Declaración de variables lateinit que se inicializarán más tarde en el ciclo de vida de la actividad
    private lateinit var databaseHelper: DatabaseHelper // Para manejar la base de datos
    private lateinit var textViewResultados: TextView   // Para mostrar los resultados de búsqueda
    private lateinit var editTextBusqueda: EditText     // Campo de texto donde el usuario ingresará el término de búsqueda

    // Método onCreate que se ejecuta cuando la actividad es creada
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_viaje) // Asigna el diseño XML a la actividad

        // Inicialización de las variables
        databaseHelper = DatabaseHelper(this) // Inicializa el ayudante de base de datos
        textViewResultados = findViewById(R.id.text_view_resultados) // Conecta el TextView con su ID en el XML
        editTextBusqueda = findViewById(R.id.edit_text_busqueda)     // Conecta el EditText con su ID en el XML

        // Configuración del botón de búsqueda, se asocia el botón con su ID y se establece un listener
        val buttonBuscar: Button = findViewById(R.id.button_buscar)
        buttonBuscar.setOnClickListener {
            // Cuando el botón es presionado, se llama al método searchViajes
            searchViajes()
        }
    }

    // Método que realiza la búsqueda de viajes en un archivo
    private fun searchViajes() {
        // Obtiene el término de búsqueda ingresado por el usuario
        val query = editTextBusqueda.text.toString().trim()

        // Busca los viajes en el archivo de texto "viajes.txt" que coincidan con el término de búsqueda
        val viajes = databaseHelper.searchViajesInFile("viajes.txt", query)

        // Crea una cadena con los detalles de los viajes encontrados
        val resultados = viajes.joinToString("\n") { viaje ->
            // Formato para mostrar la información del viaje
            "Destino: ${viaje.destino}, Fecha Inicio: ${viaje.fechaInicio}, Fecha Fin: ${viaje.fechaFin}, Actividades: ${viaje.actividades}, Presupuesto: ${viaje.presupuesto}"
        }

        // Muestra los resultados en el TextView. Si no se encontraron viajes, se muestra un mensaje por defecto
        textViewResultados.text = if (resultados.isNotEmpty()) {
            resultados // Muestra los resultados
        } else {
            "No se encontraron viajes." // Mensaje si no hay resultados
        }
    }

    // Método que busca viajes dentro de un archivo de texto y devuelve una lista de objetos Viaje
    private fun searchViajesInFile(fileName: String, searchTerm: String): List<Viaje> {
        val viajesEncontrados = mutableListOf<Viaje>() // Lista mutable para almacenar los viajes encontrados

        try {
            // Abre el archivo "viajes.txt" en modo lectura
            val fileInputStream = openFileInput(fileName)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)

            // Lee el archivo línea por línea
            bufferedReader.forEachLine { line ->
                // Divide cada línea en partes separadas por comas
                val datos = line.split(",")
                // Verifica si la línea tiene el formato correcto y si contiene el término de búsqueda en el destino o en las actividades
                if (datos.size == 6 && (datos[1].contains(searchTerm, true) || datos[4].contains(searchTerm, true))) {
                    // Si coincide, crea un objeto Viaje con los datos
                    val viaje = Viaje(
                        id = datos[0].toInt(),         // ID del viaje
                        destino = datos[1],            // Destino del viaje
                        fechaInicio = datos[2],        // Fecha de inicio del viaje
                        fechaFin = datos[3],           // Fecha de fin del viaje
                        actividades = datos[4],        // Actividades del viaje
                        presupuesto = datos[5].toDouble() // Presupuesto del viaje
                    )
                    // Añade el viaje a la lista de viajes encontrados
                    viajesEncontrados.add(viaje)
                }
            }

            // Cierra el BufferedReader
            bufferedReader.close()
        } catch (e: Exception) {
            // Captura cualquier error que ocurra durante la lectura del archivo y lo registra en el log
            Log.e("SearchViajeActivity", "Error al buscar viajes en archivo: ${e.message}")
        }

        // Devuelve la lista de viajes encontrados
        return viajesEncontrados
    }
}

