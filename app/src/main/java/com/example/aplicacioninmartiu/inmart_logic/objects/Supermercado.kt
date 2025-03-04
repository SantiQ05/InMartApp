package com.example.aplicacioninmartiu.inmart_logic.objects

import com.example.aplicacioninmartiu.inmart_logic.Inmart
import com.example.aplicacioninmartiu.inmart_logic.data_json.JSONDataBalanza
import com.example.aplicacioninmartiu.inmart_logic.data_json.JSONDataSupermercado
import com.example.aplicacioninmartiu.inmart_logic.external_connections.API
import com.example.aplicacioninmartiu.inmart_logic.external_connections.PeticionHTTP
import com.example.aplicacioninmartiu.inmart_logic.external_connections.PeticionHTTPListener


/* Clase que almacena la información referente a los supermercados (Precios y Datos) */

class Supermercado (
    private val nombreAlmacen: String
) {

    //Getter
    fun obtenerNombreAlmacen(): String {
        return nombreAlmacen
    }

    //Métodos para interactuar con el servidor y guardar los datos obtenidos en el Array

    //fun obtenerInformacionSupermercado(): String? {
        //val res = peticionHTTP.metodoPOST(API.HEROKU.getUrl(), "get-supermarket-data", "{\"storeName\": \"$nombreAlmacen\"}")
        //return res
    //}

    fun obtenerPreciosSupermercado(productoRequerido: String, callback: (String?) -> Unit) {
        val asyncPOST = PeticionHTTP.AsyncPeticionPOST(object : PeticionHTTPListener {
            override fun peticionCompleta(response: String?) {
                callback(response)
            }
        })
        asyncPOST.execute(API.HEROKU.getUrl(), "get-products-data",  "{\"storeName\": \"$nombreAlmacen\", \"productRequired\": \"$productoRequerido\"}")
    }
}