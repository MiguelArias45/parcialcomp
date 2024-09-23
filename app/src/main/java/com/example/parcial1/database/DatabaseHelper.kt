package com.example.parcial1.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor

// Clase para manejar la base de datos
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "viajes.db"
        private const val DATABASE_VERSION = 1
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
    }

    fun getAllViajes(): List<Viaje> {
        val viajes = mutableListOf<Viaje>()
        val db = readableDatabase
        val cursor: Cursor = db.query(TABLE_VIAJES, null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val viaje = Viaje(
                    id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    destino = cursor.getString(cursor.getColumnIndex(COLUMN_DESTINO)),
                    fechaInicio = cursor.getString(cursor.getColumnIndex(COLUMN_FECHA_INICIO)),
                    fechaFin = cursor.getString(cursor.getColumnIndex(COLUMN_FECHA_FIN)),
                    actividades = cursor.getString(cursor.getColumnIndex(COLUMN_ACTIVIDADES)),
                    presupuesto = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRESUPUESTO))
                )
                viajes.add(viaje)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return viajes
    }

    fun deleteViaje(id: Int) {
        val db = writableDatabase
        db.delete(TABLE_VIAJES, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
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
    }
}

