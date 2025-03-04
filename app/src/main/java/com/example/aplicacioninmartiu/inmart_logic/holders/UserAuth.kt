package com.example.aplicacioninmartiu.inmart_logic.holders

import com.example.aplicacioninmartiu.inmart_logic.objects.Usuario

object UserAuth {
    private var user: Usuario? = null

    fun cleanUser() {
        println("Cerrando sesion para usuario ${user?.obtenerCorreo()}")
        user = null
    }
    fun setUser(userAuth: Usuario) {
        user = userAuth
    }

    fun getUser(): Usuario? {
        return user
    }
}