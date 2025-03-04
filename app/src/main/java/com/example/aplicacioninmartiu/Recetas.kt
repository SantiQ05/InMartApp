package com.example.aplicacioninmartiu

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.aplicacioninmartiu.inmart_logic.Inmart
import com.example.aplicacioninmartiu.inmart_logic.holders.AppObject
import com.example.aplicacioninmartiu.inmart_logic.holders.UserAuth
import com.example.aplicacioninmartiu.inmart_logic.objects.Receta
import com.example.aplicacioninmartiu.inmart_logic.objects.Usuario
import java.net.URI

class Recetas : AppCompatActivity() {
    fun getUserAuth(): Usuario? {
        return UserAuth.getUser()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val usuarioAuth: Usuario? = getUserAuth()

        setContentView(R.layout.activity_recetas)


        val cardViewReceta = findViewById<CardView>(R.id.cardViewReceta)
        val btnCerrar = findViewById<ImageButton>(R.id.btnCerrar)

        btnCerrar.setOnClickListener {
            cardViewReceta.visibility = View.INVISIBLE
        }

        var recetasAsociadas = usuarioAuth!!.obtenerRecetasAsociadas()

        val linearLayout = findViewById<LinearLayout>(R.id.contenedorBotones)
        val txtTituloReceta = findViewById<TextView>(R.id.txtTituloReceta)
        val imgReceta = findViewById<ImageView>(R.id.imageReceta)
        val txtUrlReceta = findViewById<TextView>(R.id.txtURL)

        // Iterar sobre la lista de nombres de recetas para crear botones
        for (receta in recetasAsociadas) {
            val nuevoBoton = Button(this)
            nuevoBoton.text = receta.obtenerNombre()

            nuevoBoton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#06B2CF"))
            nuevoBoton.typeface = ResourcesCompat.getFont(this, R.font.montserrat_light)
            nuevoBoton.textAlignment = View.TEXT_ALIGNMENT_CENTER
            nuevoBoton.setTextColor(ContextCompat.getColor(this, R.color.black))

            nuevoBoton.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            nuevoBoton.setOnClickListener {

                // Acción al hacer clic en el botón (mostrar mensaje, etc.)
                nuevoBoton.setOnClickListener {
                    cardViewReceta.visibility = View.VISIBLE
                    txtTituloReceta.text = receta.obtenerNombre()
                    Glide.with(this)
                        .load(receta.obtenerUrlImagen())
                        .into(imgReceta)
                    txtUrlReceta.text = receta.obtenerUrlInfo()
                    //LOS TEXTVIEW CON LOS DATOS DE RECETA
                }
            }
            // Agregar el nuevo botón al contenedor
            linearLayout.addView(nuevoBoton)
        }

    }
}