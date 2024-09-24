package com.example.parcial1.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader

// Clase para manejar la base de datos
class DatabaseHelper(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "viajes.db"
        private const val DATABASE_VERSION = 2 // Aumentar la versión
        private const val TABLE_VIAJES = "viajes"

        const val COLUMN_ID = "id"
        const val COLUMN_DESTINO = "destino"
        const val COLUMN_FECHA_INICIO = "fecha_inicio"
        const val COLUMN_FECHA_FIN = "fecha_fin"
        const val COLUMN_ACTIVIDADES = "actividades"
        const val COLUMN_PRESUPUESTO = "presupuesto"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_VIAJES (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_DESTINO TEXT," +
                "$COLUMN_FECHA_INICIO TEXT," +
                "$COLUMN_FECHA_FIN TEXT," +
                "$COLUMN_ACTIVIDADES TEXT," +
                "$COLUMN_PRESUPUESTO REAL)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_VIAJES")
        onCreate(db)
    }

    fun addViaje(viaje: Viaje) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DESTINO, viaje.destino)
            put(COLUMN_FECHA_INICIO, viaje.fechaInicio)
            put(COLUMN_FECHA_FIN, viaje.fechaFin)
            put(COLUMN_ACTIVIDADES, viaje.actividades)
            put(COLUMN_PRESUPUESTO, viaje.presupuesto)
        }
        db.insert(TABLE_VIAJES, null, values)
        db.close()
        exportViajesToFile("viajes.txt") // Exportar a archivo después de agregar
    }

    fun getAllViajes(): List<Viaje> {
        val viajes = mutableListOf<Viaje>()
        val db = readableDatabase
        val cursor: Cursor = db.query(TABLE_VIAJES, null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                // Obtener índices
                val idIndex = cursor.getColumnIndex(COLUMN_ID)
                val destinoIndex = cursor.getColumnIndex(COLUMN_DESTINO)
                val fechaInicioIndex = cursor.getColumnIndex(COLUMN_FECHA_INICIO)
                val fechaFinIndex = cursor.getColumnIndex(COLUMN_FECHA_FIN)
                val actividadesIndex = cursor.getColumnIndex(COLUMN_ACTIVIDADES)
                val presupuestoIndex = cursor.getColumnIndex(COLUMN_PRESUPUESTO)

                // Validar que los índices sean válidos
                if (idIndex != -1 && destinoIndex != -1 && fechaInicioIndex != -1 &&
                    fechaFinIndex != -1 && actividadesIndex != -1 && presupuestoIndex != -1) {
                    val viaje = Viaje(
                        id = cursor.getInt(idIndex),
                        destino = cursor.getString(destinoIndex),
                        fechaInicio = cursor.getString(fechaInicioIndex),
                        fechaFin = cursor.getString(fechaFinIndex),
                        actividades = cursor.getString(actividadesIndex),
                        presupuesto = cursor.getDouble(presupuestoIndex)
                    )
                    viajes.add(viaje)
                } else {
                    Log.e("DatabaseHelper", "Invalid column index found")
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return viajes
    }

    fun deleteViaje(id: Int): Int {
        val db = writableDatabase
        val result = db.delete(TABLE_VIAJES, "id = ?", arrayOf(id.toString()))
        db.close()
        exportViajesToFile("viajes.txt") // Exportar a archivo después de eliminar
        return result // Devuelve el número de filas afectadas
    }

    fun updateViaje(viaje: Viaje) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DESTINO, viaje.destino)
            put(COLUMN_FECHA_INICIO, viaje.fechaInicio)
            put(COLUMN_FECHA_FIN, viaje.fechaFin)
            put(COLUMN_ACTIVIDADES, viaje.actividades)
            put(COLUMN_PRESUPUESTO, viaje.presupuesto)
        }
        db.update(TABLE_VIAJES, values, "$COLUMN_ID = ?", arrayOf(viaje.id.toString()))
        db.close()
        exportViajesToFile("viajes.txt") // Exportar a archivo después de actualizar
    }

    // Exportar viajes a un archivo de texto
    private fun exportViajesToFile(fileName: String) {
        val viajes = getAllViajes()
        val fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)

        viajes.forEach { viaje ->
            val viajeData = "${viaje.id},${viaje.destino},${viaje.fechaInicio},${viaje.fechaFin},${viaje.actividades},${viaje.presupuesto}\n"
            fileOutputStream.write(viajeData.toByteArray())
        }

        fileOutputStream.close()
    }

    // Buscar viajes en el archivo de texto
    fun searchViajesInFile(fileName: String, searchTerm: String): List<Viaje> {
        val viajesEncontrados = mutableListOf<Viaje>()
        val fileInputStream = context.openFileInput(fileName)
        val inputStreamReader = InputStreamReader(fileInputStream)
        val bufferedReader = BufferedReader(inputStreamReader)

        bufferedReader.forEachLine { line ->
            val datos = line.split(",")
            // Verificar si el término de búsqueda está en el destino o actividades
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
        return viajesEncontrados
    }
}





