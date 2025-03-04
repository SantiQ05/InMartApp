package com.example.aplicacioninmartiu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import com.example.aplicacioninmartiu.inmart_logic.Inmart
import com.example.aplicacioninmartiu.inmart_logic.holders.AppObject
import com.example.aplicacioninmartiu.inmart_logic.objects.Usuario

class InicioApp : AppCompatActivity() {

    fun createAppObject(app: Inmart) {
        AppObject.setApp(app)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicioapp)
        val app = Inmart()
        app.registrarUsuario(Usuario("Admin", "admin@example.com", "123", "Calle Ejemplo", "30526717272"))
        this.createAppObject(app)

        val btnIniciarSesion: Button = findViewById(R.id.btnIniciarSesion)
        btnIniciarSesion.setOnClickListener {

            val intentIniciarSesion: Intent = Intent(this, IniciarSesion::class.java)
            startActivity(intentIniciarSesion)
        }

        val btnRegistrarse: Button = findViewById(R.id.btnRegistrarse)
        btnRegistrarse.setOnClickListener {

            val intentRegistrarse: Intent = Intent(this, Registro::class.java)
            startActivity(intentRegistrarse)
        }
    }
}
