package com.example.aplicacioninmartiu.inmart_logic.structures

import android.os.Parcel
import android.os.Parcelable
import com.example.aplicacioninmartiu.inmart_logic.objects.Usuario

/* Arbol AVL que contiene cada uno de los usuarios registrados en la aplicación */

class ArbolUsuario<T : Comparable<T>>(
    var raiz: NodoUsuario<T>? = null
) {

    fun registrarUsuario(correoUsuario: String, usuario: Usuario) {
        var nodoCreado: NodoUsuario<T>? = null
        if (estaVacio()) {
            nodoCreado = NodoUsuario(correoUsuario = correoUsuario, usuario = usuario)
            this.raiz = nodoCreado
        } else {
            var nodoAuxiliar = this.raiz
            var padre: NodoUsuario<T>? = null

            while (nodoAuxiliar != null) {
                padre = nodoAuxiliar

                if (correoUsuario < nodoAuxiliar.obtenerCorreoUsuario()!!) {
                    nodoAuxiliar = nodoAuxiliar.obtenerNodoIzquierdo()
                } else if (correoUsuario > nodoAuxiliar.obtenerCorreoUsuario()!!) {
                    nodoAuxiliar = nodoAuxiliar.obtenerNodoDerecho()
                } else {
                    return
                }
            }

            nodoCreado = NodoUsuario(correoUsuario = correoUsuario, usuario = usuario, padre = padre)
            if (correoUsuario < padre?.obtenerCorreoUsuario()!!) {
                padre.establecerNodoIzquierdo(nodoCreado)
            } else {
                padre.establecerNodoDerecho(nodoCreado)
            }

            // Actualizar altura y realizar rotaciones si es necesario
            actualizarAltura(nodoCreado)
            fixInsert(nodoCreado)
        }
    }

    private fun actualizarAltura(nodo: NodoUsuario<T>?) {
        var nodoAuxiliar = nodo
        while (nodoAuxiliar != null) {
            nodoAuxiliar.actualizarAltura()
            nodoAuxiliar = nodoAuxiliar.obtenerPadre()
        }
    }

    private fun obtenerAlturaNodo(nodo: NodoUsuario<T>?): Int {
        return nodo?.obtenerAltura() ?: 0
    }

    private fun fixInsert(nodo: NodoUsuario<T>?) {
        var nodoAuxiliar = nodo

        while (nodoAuxiliar != null) {
            val factorEquilibrio = obtenerFactorEquilibrio(nodoAuxiliar)
            if (factorEquilibrio > 1) {
                if (obtenerAlturaNodo(nodoAuxiliar.obtenerNodoIzquierdo()?.obtenerNodoIzquierdo()) >=
                    obtenerAlturaNodo(nodoAuxiliar.obtenerNodoIzquierdo()?.obtenerNodoDerecho())) {
                    rotacionDerecha(nodoAuxiliar)
                } else {
                    rotacionIzquierdaDerecha(nodoAuxiliar)
                }
            } else if (factorEquilibrio < -1) {
                if (obtenerAlturaNodo(nodoAuxiliar.obtenerNodoDerecho()?.obtenerNodoDerecho()) >=
                    obtenerAlturaNodo(nodoAuxiliar.obtenerNodoDerecho()?.obtenerNodoIzquierdo())) {
                    rotacionIzquierda(nodoAuxiliar)
                } else {
                    rotacionDerechaIzquierda(nodoAuxiliar)
                }
            }
            nodoAuxiliar = nodoAuxiliar.obtenerPadre()
        }
    }

    private fun obtenerFactorEquilibrio(nodo: NodoUsuario<T>?): Int {
        return obtenerAlturaNodo(nodo?.obtenerNodoIzquierdo()) - obtenerAlturaNodo(nodo?.obtenerNodoDerecho())
    }

    private fun rotacionIzquierda(nodo: NodoUsuario<T>?) {
        val nuevoPadre = nodo?.obtenerNodoDerecho()
        nodo?.establecerNodoDerecho(nuevoPadre?.obtenerNodoIzquierdo())
        nuevoPadre?.obtenerNodoIzquierdo()?.establecerPadre(nodo)
        nuevoPadre?.establecerPadre(nodo.obtenerPadre())

        if (nodo?.obtenerPadre() == null) {
            raiz = nuevoPadre
        } else if (nodo == nodo.obtenerPadre()?.obtenerNodoIzquierdo()) {
            nodo.obtenerPadre()?.establecerNodoIzquierdo(nuevoPadre)
        } else {
            nodo.obtenerPadre()?.establecerNodoDerecho(nuevoPadre)
        }

        nuevoPadre?.establecerNodoIzquierdo(nodo)
        nodo?.establecerPadre(nuevoPadre)

        nodo?.actualizarAltura()
        nuevoPadre?.actualizarAltura()
    }

    private fun rotacionDerecha(nodo: NodoUsuario<T>?) {
        val nuevoPadre = nodo?.obtenerNodoIzquierdo()
        nodo?.establecerNodoIzquierdo(nuevoPadre?.obtenerNodoDerecho())
        nuevoPadre?.obtenerNodoDerecho()?.establecerPadre(nodo)
        nuevoPadre?.establecerPadre(nodo.obtenerPadre())

        if (nodo?.obtenerPadre() == null) {
            raiz = nuevoPadre
        } else if (nodo == nodo.obtenerPadre()?.obtenerNodoIzquierdo()) {
            nodo.obtenerPadre()?.establecerNodoIzquierdo(nuevoPadre)
        } else {
            nodo.obtenerPadre()?.establecerNodoDerecho(nuevoPadre)
        }

        nuevoPadre?.establecerNodoDerecho(nodo)
        nodo?.establecerPadre(nuevoPadre)

        nodo?.actualizarAltura()
        nuevoPadre?.actualizarAltura()
    }

    private fun rotacionIzquierdaDerecha(nodo: NodoUsuario<T>) {
        rotacionIzquierda(nodo.obtenerNodoIzquierdo())
        rotacionDerecha(nodo)
    }

    private fun rotacionDerechaIzquierda(nodo: NodoUsuario<T>) {
        rotacionDerecha(nodo.obtenerNodoDerecho())
        rotacionIzquierda(nodo)
    }

    fun buscarUsuario(correo: String): NodoUsuario<T>? {
        if (estaVacio()) {
            return null
        } else {
            var nodoAuxiliar = this.raiz
            while (nodoAuxiliar?.obtenerCorreoUsuario() != correo) {
                if (correo < nodoAuxiliar?.obtenerCorreoUsuario()!!) {
                    nodoAuxiliar = nodoAuxiliar.obtenerNodoIzquierdo()
                } else {
                    nodoAuxiliar = nodoAuxiliar.obtenerNodoDerecho()
                }
                if (nodoAuxiliar == null) {
                    return null
                }
            }
            return nodoAuxiliar
        }
    }

    fun estaVacio(): Boolean {
        return this.raiz == null
    }
}
