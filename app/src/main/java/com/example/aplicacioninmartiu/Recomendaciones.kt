package com.example.aplicacioninmartiu

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacioninmartiu.inmart_logic.Inmart
import com.example.aplicacioninmartiu.inmart_logic.holders.AppObject
import com.example.aplicacioninmartiu.inmart_logic.holders.UserAuth
import com.example.aplicacioninmartiu.inmart_logic.objects.Usuario

class Recomendaciones : AppCompatActivity() {

    private var progressDialog: ProgressDialog? = null

    fun getUserAuth(): Usuario? {
        return UserAuth.getUser()
    }
    fun getObjectApp(): Inmart? {
        return AppObject.getApp()
    }

    fun cleanUserAuth() {
        UserAuth.cleanUser()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {

        val app: Inmart? = getObjectApp()
        val usuarioAuth: Usuario? = getUserAuth()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recomendaciones)

        val txtUsuario = findViewById<TextView>(R.id.txtUsuario)
        txtUsuario.text = "${usuarioAuth?.obtenerNombre()}"

        val txtCorreo = findViewById<TextView>(R.id.txtCorreo)
        txtCorreo.text = "${usuarioAuth?.obtenerCorreo()}"

        val txtDireccion = findViewById<TextView>(R.id.txtDireccion)
        txtDireccion.text = "${usuarioAuth?.obtenerDireccion()}"

        val txtTelefono = findViewById<TextView>(R.id.txtTelefono)
        txtTelefono.text = "${usuarioAuth?.obtenerTelefono()}"

        val btnRecetas: Button = findViewById(R.id.btnRecetas)
        btnRecetas.setOnClickListener {
            var balanzasAsociadas = usuarioAuth!!.obtenerBalanzasAsociadas()
            var listaManualAsociada = usuarioAuth!!.obtenerListaManual()

            if(balanzasAsociadas.isNotEmpty() || listaManualAsociada.isNotEmpty()) {
                showProgressDialog()
                for(balanza in balanzasAsociadas) {
                    app!!.agregarIngrediente(balanza.obtenerProducto().obtenerNombreProducto())
                }

                for(producto in listaManualAsociada) {
                    app!!.agregarIngrediente(producto.obtenerNombreProducto())
                }

                app!!.unirVerticesGrafoRecetas(usuarioAuth) {
                    hideProgressDialog()
                    val intentRecetas: Intent = Intent(this, Recetas::class.java)
                    startActivity(intentRecetas)
                }
            }
        }

        val btnCocina: Button = findViewById(R.id.btnCocina)
        btnCocina.setOnClickListener {

            val intentCocina: Intent = Intent(this, Cocina::class.java)
            startActivity(intentCocina)
        }

        val btnCerrarSesion: Button = findViewById(R.id.btnCerrarSesion)
        btnCerrarSesion.setOnClickListener {

            app?.desactivarMedicionesSensores(usuarioAuth!!) { _ ->
                cleanUserAuth()
                val intentCerrarSesion: Intent = Intent(this, IniciarSesion::class.java)
                startActivity(intentCerrarSesion)
            }
        }
    }

    private fun showProgressDialog() {
        progressDialog = ProgressDialog(this)
        progressDialog?.setMessage("Cargando...") // Puedes personalizar el mensaje según tus necesidades
        progressDialog?.setCancelable(false)
        progressDialog?.show()
    }

    private fun hideProgressDialog() {
        progressDialog?.dismiss()
        progressDialog = null
    }


}

