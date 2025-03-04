package com.example.aplicacioninmartiu.inmart_logic.objects

class Producto (
    private var nombreProducto: String,
    private var precioProducto: String? = null
) {

    private var tiempoPromedioAgotamiento: Int = 0

    fun obtenerNombreProducto(): String {
        return nombreProducto
    }

    fun obtenerPrecioProducto(): Double {
        return this.precioProducto!!.toDouble()
    }

    fun calcularPronostico() {
        tiempoPromedioAgotamiento = 1
    }

    override fun toString(): String {
        return "Producto: $nombreProducto - $$precioProducto"
    }


}