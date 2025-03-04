package com.example.aplicacioninmartiu.ui.market

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.aplicacioninmartiu.IniciarSesion
import com.example.aplicacioninmartiu.Mercado
import com.example.aplicacioninmartiu.R
import com.example.aplicacioninmartiu.databinding.FragmentMarketBinding
import com.example.aplicacioninmartiu.inmart_logic.Inmart
import com.example.aplicacioninmartiu.inmart_logic.holders.AppObject
import com.example.aplicacioninmartiu.inmart_logic.holders.UserAuth
import com.example.aplicacioninmartiu.inmart_logic.objects.Usuario

class MarketFragment : Fragment() {

    private var _binding: FragmentMarketBinding? = null
    private val binding get() = _binding!!

    fun getObjectApp(): Inmart? {
        return AppObject.getApp()
    }

    fun getUserAuth(): Usuario? {
        return UserAuth.getUser()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentMarketBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val app: Inmart? = getObjectApp()
        val usuarioAuth: Usuario? = getUserAuth()

        val containerSupermercado = binding.contenedorImagenes


        val carritoMercadoAsociado = usuarioAuth!!.obtenerCarritoMercado()
        val idsMercadosCreados = usuarioAuth!!.obtenerSupermercados()

        if(idsMercadosCreados.isNotEmpty()) {
            for(id in idsMercadosCreados) {
                val nuevoBoton = ImageButton(requireContext())
                nuevoBoton.id = id

                nuevoBoton.textAlignment = View.TEXT_ALIGNMENT_CENTER

                // Configuración de la imagen según el supermercado
                val strSupermercado: String

                when (id) {
                    R.id.boton_exito -> {
                        strSupermercado = "Exito"
                        nuevoBoton.setImageResource(R.drawable.exitoimage)
                    }
                    R.id.boton_carulla -> {
                        strSupermercado = "Carulla"
                        nuevoBoton.setImageResource(R.drawable.carullaimage)
                    }
                    R.id.boton_colsubsidio -> {
                        strSupermercado = "Colsubsidio"
                        nuevoBoton.setImageResource(R.drawable.colsubimage)
                    }
                    R.id.boton_jumbo -> {
                        strSupermercado = "Jumbo"
                        nuevoBoton.setImageResource(R.drawable.jumboimage)
                    }
                    else -> {
                        strSupermercado = ""
                    }
                }

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(20, 20, 20, 20)
                nuevoBoton.layoutParams = params

                val heightInPixels = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    180f,
                    resources.displayMetrics
                ).toInt()

                val layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    heightInPixels
                )
                nuevoBoton.layoutParams = layoutParams

                nuevoBoton.setOnClickListener {
                    // Acción al hacer clic en el botón (mostrar mensaje, etc.)
                    val intentRegistrado: Intent = Intent(requireContext(), Mercado::class.java)
                    intentRegistrado.putExtra("strSupermercado", strSupermercado)
                    startActivity(intentRegistrado)
                }

                // Agregar el nuevo botón al contenedor
                containerSupermercado.addView(nuevoBoton)
            }
        }

        if(carritoMercadoAsociado.isNotEmpty()) {
            for (producto in carritoMercadoAsociado) {
                val idBoton: Int
                val strSupermercado: String
                when (producto.obtenerSupermercadoCarrito()) {
                    "Exito" -> idBoton = R.id.boton_exito
                    "Carulla" -> idBoton = R.id.boton_carulla
                    "Colsubsidio" -> idBoton = R.id.boton_colsubsidio
                    "Jumbo" -> idBoton = R.id.boton_jumbo
                    else -> continue  // Si el supermercado no coincide con ninguno, continuar con el siguiente producto
                }

                // Verificar si el botón con el ID ya fue creado
                if (idBoton !in idsMercadosCreados) {
                    val nuevoBoton = ImageButton(requireContext())
                    nuevoBoton.id = idBoton

                    nuevoBoton.textAlignment = View.TEXT_ALIGNMENT_CENTER

                    // Configuración de la imagen según el supermercado
                    when (producto.obtenerSupermercadoCarrito()) {
                        "Exito" -> {
                            strSupermercado = "Exito"
                            nuevoBoton.setImageResource(R.drawable.exitoimage)
                        }
                        "Carulla" -> {
                            strSupermercado = "Carulla"
                            nuevoBoton.setImageResource(R.drawable.carullaimage)
                        }
                        "Colsubsidio" -> {
                            strSupermercado = "Colsubsidio"
                            nuevoBoton.setImageResource(R.drawable.colsubimage)
                        }
                        "Jumbo" -> {
                            strSupermercado = "Jumbo"
                            nuevoBoton.setImageResource(R.drawable.jumboimage)
                        }
                        else -> {
                            strSupermercado = ""
                        }
                    }

                    val heightInPixels = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        180f,
                        resources.displayMetrics
                    ).toInt()

                    val layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        heightInPixels
                    )
                    nuevoBoton.layoutParams = layoutParams

                    nuevoBoton.setOnClickListener {
                        // Acción al hacer clic en el botón (mostrar mensaje, etc.)
                        val intentRegistrado: Intent = Intent(requireContext(), Mercado::class.java)
                        intentRegistrado.putExtra("strSupermercado", strSupermercado)
                        startActivity(intentRegistrado)
                    }

                    // Agregar el nuevo botón al contenedor
                    containerSupermercado.addView(nuevoBoton)

                    // Agregar el ID del botón creado al conjunto
                    usuarioAuth!!.añadirSupermercado(idBoton)
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
