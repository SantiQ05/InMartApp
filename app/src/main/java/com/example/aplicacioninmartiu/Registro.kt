package com.example.aplicacioninmartiu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.aplicacioninmartiu.inmart_logic.Inmart
import com.example.aplicacioninmartiu.inmart_logic.holders.AppObject
import com.example.aplicacioninmartiu.inmart_logic.objects.Usuario

class Registro : AppCompatActivity() {

    fun getObjectApp(): Inmart? {
        return AppObject.getApp()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        val app: Inmart? = getObjectApp()

        val Nombre: EditText = findViewById(R.id.Nombre)

        Nombre.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val inputText = s.toString().trim()
                if (inputText.isEmpty()) {
                    Nombre.error = "Este campo no puede estar vacío"
                } else {
                    Nombre.error = null
                }
            }
        })
        val correoElectronicoR: EditText = findViewById(R.id.correoElectronicoR)

        correoElectronicoR.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val inputText = s.toString().trim()
                if (inputText.isEmpty()) {
                    correoElectronicoR.error = "Este campo no puede estar vacío"
                } else {
                    correoElectronicoR.error = null
                }
            }
        })
        val contraseñaR: EditText = findViewById(R.id.contraseñaR)

        contraseñaR.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val inputText = s.toString().trim()
                if (inputText.isEmpty()) {
                    contraseñaR.error = "Este campo no puede estar vacío"
                } else {
                    contraseñaR.error=null
                }
            }
        })
        val Direccion: EditText = findViewById(R.id.Direccion)

        Direccion.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val inputText = s.toString().trim()
                if (inputText.isEmpty()) {
                    Direccion.error = "Este campo no puede estar vacío"
                } else {
                    Direccion.error = null
                }
            }
        })
        val Telefono: EditText = findViewById(R.id.Telefono)

        Telefono.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val inputText = s.toString().trim()
                if (inputText.isEmpty()) {
                    Telefono.error = "Este campo no puede estar vacío"
                } else {
                    Telefono.error = null
                }
            }
        })

        val btnUsuarioRegistrado: Button = findViewById(R.id.btnUsuarioRegistrado)
        btnUsuarioRegistrado.setOnClickListener {
            if (Nombre.text.toString().isNotEmpty() && correoElectronicoR.text.toString().isNotEmpty() && contraseñaR.text.toString().isNotEmpty() && Direccion.text.toString().isNotEmpty() && Telefono.text.toString().isNotEmpty()) {

                val registered = app?.registrarUsuario(
                                    Usuario(
                                        Nombre.text.toString(),
                                        correoElectronicoR.text.toString(),
                                        contraseñaR.text.toString(),
                                        Direccion.text.toString(),
                                        Telefono.text.toString()
                                    )
                                )

                if (registered!!) {
                    val intentRegistrado: Intent = Intent(this, IniciarSesion::class.java)
                    startActivity(intentRegistrado)
                } else {
                    val errorTextView = findViewById<TextView>(R.id.errorMessageTextView2)
                    errorTextView.text = "El usuario ya se encuentra registrado"
                    errorTextView.visibility = View.VISIBLE
                }
            } else {
                val errorTextView = findViewById<TextView>(R.id.errorMessageTextView2)
                errorTextView.text = "Los campos no pueden estar vacios"
                errorTextView.visibility = View.VISIBLE
            }
        }
    }
}
