package com.example.aplicacioninmartiu

import android.annotation.SuppressLint
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
import com.example.aplicacioninmartiu.inmart_logic.external_connections.DominioSupermercado
import com.example.aplicacioninmartiu.inmart_logic.holders.AppObject
import com.example.aplicacioninmartiu.inmart_logic.holders.UserAuth
import com.example.aplicacioninmartiu.inmart_logic.objects.Supermercado
import com.example.aplicacioninmartiu.inmart_logic.objects.Usuario


class IniciarSesion : AppCompatActivity() {

    fun getObjectApp(): Inmart? {
        return AppObject.getApp()
    }

    fun createUserAuth(userAuth: Usuario) {
        UserAuth.setUser(userAuth)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iniciar_sesion)
        val app: Inmart? = getObjectApp()

        val btnRegistrate: Button = findViewById(R.id.btnRegistrate)
        btnRegistrate.setOnClickListener {

            val intentRegistrate: Intent = Intent(this, Registro::class.java)
            startActivity(intentRegistrate)
        }
        val CorreoElectronicoIS: EditText = findViewById(R.id.CorreoElectronicoIS)

        CorreoElectronicoIS.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val inputText = s.toString().trim()
                if (inputText.isEmpty()) {
                    CorreoElectronicoIS.error = "Este campo no puede estar vacío"
                } else {
                    CorreoElectronicoIS.error = null
                }
            }
        })
        val ContraseñaIS: EditText = findViewById(R.id.ContraseñaIS)

        ContraseñaIS.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val inputText = s.toString().trim()
                if (inputText.isEmpty()) {
                    ContraseñaIS.error = "Este campo no puede estar vacío"
                } else {
                    ContraseñaIS.error = null
                }
            }
        })

        val btnUsuarioIniciaSesion: Button = findViewById(R.id.btnUsuarioIniciaSesion)
        btnUsuarioIniciaSesion.setOnClickListener {
            if (CorreoElectronicoIS.text.toString().isNotEmpty() && ContraseñaIS.text.toString().isNotEmpty()) {
                val usuarioAutenticado = app?.autenticarUsuario(CorreoElectronicoIS.text.toString(), ContraseñaIS.text.toString())
                if (usuarioAutenticado != null) {
                    createUserAuth(usuarioAutenticado)
                    val intentInicioSesion: Intent = Intent(this, PaginaHome::class.java)
                    startActivity(intentInicioSesion)
                } else {
                    val errorTextView = findViewById<TextView>(R.id.errorMessageTextView)
                    errorTextView.text = "El usuario o contraseña son incorrectos"
                    errorTextView.visibility = View.VISIBLE
                }
            } else {
                val errorTextView = findViewById<TextView>(R.id.errorMessageTextView)
                errorTextView.text = "Los campos no pueden estar vacios"
                errorTextView.visibility = View.VISIBLE
            }
        }
    }

    override fun onStop() {
        super.onStop()
        finishAffinity()
    }
}