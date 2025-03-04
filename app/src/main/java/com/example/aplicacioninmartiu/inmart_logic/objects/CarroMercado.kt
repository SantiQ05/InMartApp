package com.example.aplicacioninmartiu.inmart_logic.objects

class CarroMercado (
    private val producto: Producto,
    private val supermercado: String
){
    fun obtenerProductoCarrito(): Producto {
        return this.producto
    }

    fun obtenerSupermercadoCarrito(): String {
        return this.supermercado
    }

    override fun toString(): String {
        return """
                Carro Mercado:
                Producto: $producto,
                Supermercado: $supermercado
            """.trimIndent()
    }


}