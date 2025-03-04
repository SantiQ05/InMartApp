package com.example.aplicacioninmartiu.inmart_logic.external_connections

enum class DominioSupermercado {

    //Creamos el ENUM con los dominios de los supermercados
    EXITO("exito"),
    JUMBO("tiendasjumbo"),
    CARULLA("carulla"),
    COLSUBSIDIO("mercadocolsubsidio");

    //Creamos los metodos para obtener los dominios como String
    private val dominio: String

    constructor(dominio: String) {
        this.dominio = dominio
    }

    fun getDominio(): String {
        return this.dominio
    }
}