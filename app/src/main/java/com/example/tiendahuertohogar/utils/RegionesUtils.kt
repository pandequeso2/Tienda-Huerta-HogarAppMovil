package com.example.tiendahuertohogar.utils


// Datos de ejemplo para que el proyecto compile
val regionesDeChile = listOf(
    "Metropolitana",
    "Valparaíso",
    "Biobío",
    "La Araucanía"
)

fun obtenerComunas(region: String): List<String> {
    return when (region) {
        "Metropolitana" -> listOf("Santiago", "Puente Alto", "Maipú", "La Florida")
        "Valparaíso" -> listOf("Valparaíso", "Viña del Mar", "Quilpué")
        "Biobío" -> listOf("Concepción", "Talcahuano", "Los Ángeles")
        "La Araucanía" -> listOf("Temuco", "Villarrica", "Pucón")
        else -> emptyList()
    }
}