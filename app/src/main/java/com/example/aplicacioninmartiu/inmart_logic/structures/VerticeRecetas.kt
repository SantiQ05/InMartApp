package com.example.aplicacioninmartiu.inmart_logic.structures

import com.example.aplicacioninmartiu.inmart_logic.objects.Receta

data class VerticeRecetas (private val elemento: Receta) {

    fun obtenerRecetaVertice(): Receta {
        return elemento
    }
    fun obtenerNombreElemento(): String {
        return elemento.obtenerNombre()
    }

    fun elementoEsIngrediente(): Boolean {
        return elemento.esIngrediente()
    }

    fun obtenerPesoReceta(): Int {
        return elemento.obtenerTiempoPreparacion()
    }

    override fun toString(): String {
        return "Vertice: $elemento)"
    }
}