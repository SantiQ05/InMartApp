package com.example.aplicacioninmartiu.inmart_logic.objects

class Receta (
    private val nombre: String,
    private val urlImagen: String? = null,
    private val urlInformacion: String? = null,
    private val esIngrediente: Boolean = true,
    private val tiempoPreparacion: Int = 1
) {

    fun obtenerNombre(): String {
        return nombre
    }

    fun obtenerUrlImagen(): String? {
        return urlImagen
    }

    fun obtenerUrlInfo(): String? {
        return urlInformacion
    }

    fun esIngrediente(): Boolean {
        return esIngrediente
    }

    fun obtenerTiempoPreparacion(): Int {
        return tiempoPreparacion
    }

    override fun toString(): String {
        return "Receta: Nombre: '$nombre', Imagen: $urlImagen, Informacion: $urlInformacion, esIngrediente: $esIngrediente)"
    }


}