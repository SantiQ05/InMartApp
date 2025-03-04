package com.example.aplicacioninmartiu.inmart_logic.structures

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.aplicacioninmartiu.inmart_logic.Inmart
import com.example.aplicacioninmartiu.inmart_logic.external_connections.API
import com.example.aplicacioninmartiu.inmart_logic.external_connections.PeticionHTTP
import com.example.aplicacioninmartiu.inmart_logic.external_connections.PeticionHTTPListener
import com.example.aplicacioninmartiu.inmart_logic.objects.Receta
import java.util.PriorityQueue

class GrafoRecetas {
    private val listaAdyacencia: MutableMap<VerticeRecetas, MutableList<AristaRecetas>> = mutableMapOf()


    fun agregarIngrediente(nombreIngrediente: String) {
        val nuevoVertice = VerticeRecetas(Receta(nombreIngrediente))
        if (!listaAdyacencia.containsKey(nuevoVertice)) {
            listaAdyacencia[nuevoVertice] = mutableListOf()
        }
    }

    fun asociarIngredienteConReceta(ingrediente: VerticeRecetas, nombre: String, imagen: String, informacion: String, tiempoPreparacion: Int) {
        //Las relaciones en el grafo se constituyen a través de la asociación de un vertice de ingrediente a uno de una receta
        val nuevoVerticeReceta = VerticeRecetas(Receta(nombre, imagen, informacion, false, tiempoPreparacion))
        if (listaAdyacencia.containsKey(ingrediente)) {
            val arista = AristaRecetas(nuevoVerticeReceta, nuevoVerticeReceta.obtenerPesoReceta())
            listaAdyacencia[ingrediente]?.add(arista)
        }
    }

    fun buscarRecetas(ingredientes: String, callback: (String?) -> Unit) {
        //Peticion a la API Spoonacular para obtener las recetas basado en los ingredientes disponibles
        val asyncPOST = PeticionHTTP.AsyncPeticionPOST(object : PeticionHTTPListener {
            override fun peticionCompleta(response: String?) {
                callback(response)
            }
        })
        asyncPOST.execute(API.HEROKU.getUrl(), "get-recipes-data", "{\"ingredients\": \"$ingredientes\"}")
    }

    fun obtenerListaAdyacencia(): MutableMap<VerticeRecetas, MutableList<AristaRecetas>> {
        return listaAdyacencia
    }

    fun obtenerIngredientesBusqueda(): MutableList<VerticeRecetas> {
        //Retornamos todos los elementos que coincidan con un ingrediente
        val listaIngredientes = mutableListOf<VerticeRecetas>()
        for ((elemento, _) in listaAdyacencia) {
            if(elemento.elementoEsIngrediente()) {
                listaIngredientes.add(elemento)
            }
        }
        return listaIngredientes
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun dijkstra(origen: VerticeRecetas): Map<VerticeRecetas, Int> {
        val distancias: MutableMap<VerticeRecetas, Int> = mutableMapOf()
        val visitados: MutableSet<VerticeRecetas> = mutableSetOf()

        // Cola de prioridad para seleccionar el vértice con la distancia mínima para organizarlos por peso
        val colaPrioridad = PriorityQueue<VerticeRecetas>(compareBy { distancias[it] })

        //Obtenemos los valores del peso (Tiempos de preparación provenientes de la API)
        var primerElementoEnVerticeReceta = true
        for (recetasAsociadas in listaAdyacencia.values) {
            for (verticeReceta in recetasAsociadas) {
                if (primerElementoEnVerticeReceta) {
                    colaPrioridad.add(verticeReceta.obtenerConexionArista())
                    distancias[verticeReceta.obtenerConexionArista()] = verticeReceta.obtenerPesoArista()
                    primerElementoEnVerticeReceta = false
                } else {
                    distancias[verticeReceta.obtenerConexionArista()] = Int.MAX_VALUE
                }
            }
        }

        //Construimos y retornamos una lista que contiene las recetas ordenadas por su peso y filtrando las posibles coincidencias
        while (colaPrioridad.isNotEmpty()) {
            val actual = colaPrioridad.poll()

            visitados.add(actual)

            for (arista in listaAdyacencia[origen]!!) {
                val vecino = arista.obtenerConexionArista()
                if (vecino !in visitados) {
                    val nuevaDistancia = distancias[actual]!! + arista.obtenerPesoArista()
                    if (nuevaDistancia < distancias[vecino]!!) {
                        distancias[vecino] = nuevaDistancia
                        colaPrioridad.add(vecino)
                    }
                }
            }
        }
        return distancias
    }
}