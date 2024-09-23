package com.example.parcial1.database

// Clase que representa un viaje
data class Viaje(
    val id: Int = 0, // El id puede ser 0 para nuevos viajes
    val destino: String,
    val fechaInicio: String,
    val fechaFin: String,
    val actividades: String,
    val presupuesto: Double
)
