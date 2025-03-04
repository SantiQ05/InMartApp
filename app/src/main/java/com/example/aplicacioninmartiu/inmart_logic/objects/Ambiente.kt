package com.example.aplicacioninmartiu.inmart_logic.objects

data class Ambiente(
    private var temperatura: Double = 0.0,
    private var humedad: Double = 0.0,
    private var methane_gas: Double = 0.0
) {

    fun obtenerTemperatura(): Double {
        return this.temperatura
    }

    fun obtenerHumedad(): Double {
        return this.humedad
    }

    fun obtenerGasMetano(): Double {
        return this.methane_gas
    }
}
