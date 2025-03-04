package com.example.aplicacioninmartiu.inmart_logic.objects

data class Balanza(
    private var productoPesado: Producto,
    private var pesoInicialRegistrado: Double = 0.0,
    private var pesoActualRegistrado: Double = 0.0,
) {
    companion object {
        //Contador de tipo Static para incrementar el Id y asegurar que sea único para cada balanza creada
        private var contador: Int = 0
    }

    //Atributo interno
    private val id: Int

    //Constructor complementario que instancia el Id en el objeto
    init {
        contador++
        this.id = contador
    }

    fun obtenerId(): Int {
        return id
    }

    fun obtenerProducto(): Producto {
        return this.productoPesado
    }

    fun obtenerPesoInicial(): Double {
        return this.pesoInicialRegistrado
    }

    fun obtenerPesoRestante(): Double {
        return this.pesoActualRegistrado
    }

    fun registrarPesoInicial(pesoRegistrado: Double) {
        this.pesoInicialRegistrado = pesoRegistrado
    }

    fun registrarPesoRestante(pesoRestante: Double) {
        this.pesoActualRegistrado = pesoRestante
    }

    fun actualizarProductoPesado(productoPesadoActualizado: Producto) {
        this.productoPesado = productoPesadoActualizado
    }

    fun chequearProductoPesado(): Boolean {
        val diferencia = (this.pesoActualRegistrado/this.pesoInicialRegistrado)*100
        if(diferencia <= 20 && diferencia != 0.0) {
            return true
        }
        return false
    }
}
