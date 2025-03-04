package com.example.aplicacioninmartiu.inmart_logic.external_connections

enum class API {
    //Creamos el ENUM con las URL de los servidores externos
    HEROKU("https://inmart-project-e0a612848a08.herokuapp.com/");

    //Creamos los metodos para obtener las diferentes URL como String
    private val urlBase: String

    constructor(urlBase: String) {
        this.urlBase = urlBase
    }

    fun getUrl(): String {
        return this.urlBase
    }
}