package com.example.aplicacioninmartiu.inmart_logic.structures

import android.os.Parcel
import android.os.Parcelable
import com.example.aplicacioninmartiu.inmart_logic.objects.Usuario

/* Nodo que contiene cada uno de los usuarios contenidos en el árbol AVL */

class NodoUsuario<T : Comparable<T>>(
    private var correoUsuario: String?,
    private var usuario: Usuario,
    private var altura: Int = 1,
    private var padre: NodoUsuario<T>? = null,
    private var nodoIzquierdo: NodoUsuario<T>? = null,
    private var nodoDerecho: NodoUsuario<T>? = null
) {

    fun obtenerCorreoUsuario(): String? {
        return this.correoUsuario
    }

    fun obtenerUsuario(): Usuario {
        return this.usuario
    }

    fun obtenerAltura(): Int {
        return this.altura
    }

    fun actualizarAltura() {
        val alturaIzquierdo = obtenerAlturaNodo(nodoIzquierdo)
        val alturaDerecho = obtenerAlturaNodo(nodoDerecho)
        this.altura = 1 + maxOf(alturaIzquierdo, alturaDerecho)
    }

    private fun obtenerAlturaNodo(nodo: NodoUsuario<T>?): Int {
        return nodo?.altura ?: 0
    }

    fun obtenerPadre(): NodoUsuario<T>? {
        return this.padre
    }

    fun establecerPadre(padre: NodoUsuario<T>?) {
        this.padre = padre
    }

    fun establecerNodoIzquierdo(nodoIzquierdo: NodoUsuario<T>?) {
        this.nodoIzquierdo = nodoIzquierdo
        nodoIzquierdo?.establecerPadre(this)
        actualizarAltura()
    }

    fun obtenerNodoIzquierdo(): NodoUsuario<T>? {
        return this.nodoIzquierdo
    }

    fun establecerNodoDerecho(nodoDerecho: NodoUsuario<T>?) {
        this.nodoDerecho = nodoDerecho
        nodoDerecho?.establecerPadre(this)
        actualizarAltura()
    }

    fun obtenerNodoDerecho(): NodoUsuario<T>? {
        return this.nodoDerecho
    }
}

