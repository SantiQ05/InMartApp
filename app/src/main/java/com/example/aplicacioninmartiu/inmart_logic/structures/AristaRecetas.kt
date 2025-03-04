package com.example.aplicacioninmartiu.inmart_logic.structures

data class AristaRecetas(
    private val conexion: VerticeRecetas,
    private val peso: Int, //Tiempo en el cual la receta se puede hacer
) {

    fun obtenerConexionArista(): VerticeRecetas {
        return conexion
    }

    fun obtenerPesoArista(): Int {
        return peso
    }

    override fun toString(): String {
        return "Arista: Conexion: $conexion, Peso: $peso)"
    }
}