package com.example.aplicacioninmartiu.inmart_logic

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.aplicacioninmartiu.inmart_logic.data_json.JSONDataBalanza
import com.example.aplicacioninmartiu.inmart_logic.data_json.JSONDataReceta
import com.example.aplicacioninmartiu.inmart_logic.data_json.JSONDataSupermercado
import com.example.aplicacioninmartiu.inmart_logic.data_json.JSONDataAmbiente
import com.example.aplicacioninmartiu.inmart_logic.external_connections.API
import com.example.aplicacioninmartiu.inmart_logic.external_connections.PeticionHTTP
import com.example.aplicacioninmartiu.inmart_logic.external_connections.PeticionHTTPListener
import com.example.aplicacioninmartiu.inmart_logic.objects.Ambiente
import com.example.aplicacioninmartiu.inmart_logic.objects.Balanza
import com.example.aplicacioninmartiu.inmart_logic.objects.Producto
import com.example.aplicacioninmartiu.inmart_logic.objects.Supermercado
import com.example.aplicacioninmartiu.inmart_logic.objects.Usuario
import com.example.aplicacioninmartiu.inmart_logic.structures.ArbolUsuario
import com.example.aplicacioninmartiu.inmart_logic.structures.GrafoRecetas
import com.google.gson.Gson

/* Clase que administra el aplicativo de software en general */
class Inmart() {

    private var arbolUsuarios = ArbolUsuario<String>()
    private val gson = Gson() //Libreria para procesamiento de diccionarios JSON provenientes del server
    private val grafoRecetas = GrafoRecetas()

    /* Metodos para administracion de usuarios en el aplicativo */

    fun registrarUsuario(usuario: Usuario): Boolean {
        val usuarioEncontrado = arbolUsuarios.buscarUsuario(usuario.obtenerCorreo())
        return if(usuarioEncontrado == null) {
            arbolUsuarios.registrarUsuario(correoUsuario = usuario.obtenerCorreo(), usuario = usuario)
            println("Se registro el usuario: ${usuario.obtenerCorreo()}")
            true
        } else {
            false
        }
    }

    fun autenticarUsuario(correo: String, contrasena: String): Usuario? {
        val usuarioEncontrado = arbolUsuarios.buscarUsuario(correo)

        // Si el usuario ingresado existe y sus credenciales son correctas, retornamos true
        return if (usuarioEncontrado != null && usuarioEncontrado.obtenerUsuario().obtenerContrasena() == contrasena) {
            println("Se autenticó el usuario: ${usuarioEncontrado.obtenerUsuario().obtenerCorreo()}")
            usuarioEncontrado.obtenerUsuario()
        } else {
            println("Ningun usuario encontrado para autenticar")
            null
        }
    }

    /* Metodos para obtención de datos Arduino */
    fun activarMedicionesSensoresPeso(usuarioAuth: Usuario, callback: (String?) -> Unit) {
        val balanzasVinculadas = usuarioAuth?.obtenerBalanzasAsociadas()
        var listaBalanzasActivas = mutableListOf<Int>()
        if(balanzasVinculadas!!.isNotEmpty()) {
            for(balanza in balanzasVinculadas) {
                listaBalanzasActivas.add(balanza.obtenerId())
            }
            val asyncGET = PeticionHTTP.AsyncPeticionGET(object: PeticionHTTPListener {
                override fun peticionCompleta(result: String?) {
                    callback(result)
                }
            })
            asyncGET.execute(API.HEROKU.getUrl(), "activate-weight-user?idUser=${usuarioAuth?.obtenerId()}")
        }
    }
    fun activarMedicionesSensoresAmbiente(usuarioAuth: Usuario, callback: (String?) -> Unit) {
        val asyncGET = PeticionHTTP.AsyncPeticionGET(object: PeticionHTTPListener {
            override fun peticionCompleta(result: String?) {
                callback(result)
            }
        })
        asyncGET.execute(API.HEROKU.getUrl(), "activate-environment-user?idUser=${usuarioAuth?.obtenerId()}")
    }

    fun desactivarMedicionesSensores(usuarioAuth: Usuario, callback: (String?) -> Unit) {
        val asyncGET = PeticionHTTP.AsyncPeticionGET(object: PeticionHTTPListener {
            override fun peticionCompleta(response: String?) {
                callback(response)
            }
        })
        asyncGET.execute(API.HEROKU.getUrl(), "delete-user?idUser=${usuarioAuth.obtenerId()}")
    }

    fun registrarProductosBalanza(producto: Producto, usuarioAuth: Usuario) {
        val nuevaBalanza = Balanza(producto)
        usuarioAuth.asociarBalanza(nuevaBalanza)
    }

    fun obtenerMedicionesSensoresPeso(usuarioAuth: Usuario, callback: (String?) -> Unit) {
        val balanzasAsociadas = usuarioAuth.obtenerBalanzasAsociadas()
        val asyncPOST = PeticionHTTP.AsyncPeticionPOST(object : PeticionHTTPListener {
            override fun peticionCompleta(response: String?) {
                val jsonData = gson.fromJson(response, JSONDataBalanza::class.java)
                val pesosRegistrados = jsonData.data
                for (i in pesosRegistrados.indices) {
                    val pesoRegistrado = pesosRegistrados[i]
                    val balanza = balanzasAsociadas[i]
                    if(balanza.obtenerPesoInicial() == pesoRegistrado.weight.toDouble() || balanza.obtenerPesoInicial() == 0.0) {
                        if(pesoRegistrado.weight.toDouble() > 0.0) {
                            balanza.registrarPesoInicial(pesoRegistrado.weight.toDouble())
                            println("REGISTRANDO PESO INICIAL O SIGUE IGUAL ${pesoRegistrado.weight}")
                        } else {
                            balanza.registrarPesoInicial(0.0)
                            balanza.registrarPesoRestante(0.0)
                        }
                    } else {
                        balanza.registrarPesoRestante(pesoRegistrado.weight.toDouble())
                        println("EL PESO CAMBIOOO ${pesoRegistrado.weight}")
                    }

                }
                callback(response)
            }
        })

        val listaIdsBalanza = mutableListOf<Int>()
        for (balanza in balanzasAsociadas) {
            listaIdsBalanza.add(balanza.obtenerId())
        }
        asyncPOST.execute(API.HEROKU.getUrl(), "get-weight-sensors",  "{\"idUsuario\": \"${usuarioAuth.obtenerId()}\"}")
    }

    fun obtenerMedicionesSensoresAmbiente(usuarioAuth: Usuario, callback: (String?) -> Unit) {
        val asyncPOST = PeticionHTTP.AsyncPeticionPOST(object : PeticionHTTPListener {
            override fun peticionCompleta(response: String?) {
                val jsonData = gson.fromJson(response, JSONDataAmbiente::class.java)
                for (condicion in jsonData.data) {
                   usuarioAuth.añadirEstadisticaAmbiental(Ambiente(condicion.temperature.toDouble(), condicion.humidity.toDouble(), condicion.methane_gas.toDouble()))
                }
                callback(response)
            }
        })
        asyncPOST.execute(API.HEROKU.getUrl(), "get-environment-sensors",  "{\"idUsuario\": \"${usuarioAuth.obtenerId()}\"}")
    }

    /* Funciones para creacion y obtencion de recetas utilizando el grafo */
    fun agregarIngrediente(ingrediente: String) {
        grafoRecetas.agregarIngrediente(ingrediente)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun unirVerticesGrafoRecetas(usuarioAuth: Usuario, callback: () -> Unit) {
        //Al buscar una receta se obtienen los ingredientes disponibles para asociarlas
        val listaIngredientes = grafoRecetas.obtenerIngredientesBusqueda()
        var count = 0

        for (ingrediente in listaIngredientes) {
            grafoRecetas.buscarRecetas(ingrediente.obtenerNombreElemento())
            { response ->
                if(response != null) {
                    count++
                    val recetas = gson.fromJson(
                        response,
                        JSONDataReceta::class.java
                    )
                    for(receta in recetas.data) {
                        grafoRecetas.asociarIngredienteConReceta(ingrediente, receta.title, receta.image, receta.info, receta.time)
                    }
                    if(listaIngredientes.size == count) {
                        this.filtrarRecetasPorTiempo(usuarioAuth)
                        callback()
                    }
                } else {
                    count++
                    if(listaIngredientes.size == count) {
                        this.filtrarRecetasPorTiempo(usuarioAuth)
                        callback()
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun filtrarRecetasPorTiempo(usuarioAuth: Usuario) {
        //A través del algoritmo de dijkstra recorremos el grafo por sus pesos para obtener un filtro
        val vertices = grafoRecetas.obtenerListaAdyacencia().keys

        for (vertice in vertices) {
            val distanciasDesdeIngrediente = grafoRecetas.dijkstra(vertice)

            // Filtrar y ordenar las recetas por peso (El mejor tiempo)
            val recetasOrdenadas = distanciasDesdeIngrediente
                .filter { !it.key.elementoEsIngrediente() }
                .entries
                .sortedBy { it.value }

            val listaRecetasAsociadas = usuarioAuth.obtenerRecetasAsociadas()

            for ((elemento, peso) in recetasOrdenadas) {
                if(peso != Int.MAX_VALUE) {
                    if (!listaRecetasAsociadas.contains(elemento.obtenerRecetaVertice())) {
                        usuarioAuth.asociarReceta(elemento.obtenerRecetaVertice())
                    }

                }
            }
        }
    }

    /* Funciones que se encargan de realizar el WebScraping */

    fun obtenerPreciosSupermercado(usuarioAuth: Usuario, supermercado: Supermercado, productoRequerido: String, callback: () -> Unit) {
        val productosEncontrados = mutableListOf<Producto>()
        supermercado.obtenerPreciosSupermercado(productoRequerido)
            {
            response ->
                val productos = gson.fromJson(
                    response,
                    JSONDataSupermercado::class.java
                )

                for(producto in productos.data) {
                    productosEncontrados.add(Producto(producto.brand, producto.price))
                }
                usuarioAuth.asociarProductoSupermercado(supermercado.obtenerNombreAlmacen(), productosEncontrados)
                callback()
        }
    }

    /* Funciones que se encargan de gestionar el Carro de Mercado del usuario */

    fun calcularTotalCarrito(usuarioAuth: Usuario, listaProductosTotalizados: List<Double>): Double {
        var carritoMercadoAsociado = listaProductosTotalizados
        var totalCarrito = 0.0
        if(carritoMercadoAsociado.isNotEmpty()) {
            for (elementoCarrito in carritoMercadoAsociado) {
                totalCarrito += elementoCarrito
            }
        }
        return totalCarrito
    }

}
