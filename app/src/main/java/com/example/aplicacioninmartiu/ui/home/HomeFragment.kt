package com.example.aplicacioninmartiu.ui.home

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.aplicacioninmartiu.Recomendaciones
import com.example.aplicacioninmartiu.databinding.FragmentHomeBinding
import com.example.aplicacioninmartiu.inmart_logic.Inmart
import com.example.aplicacioninmartiu.inmart_logic.holders.AppObject
import com.example.aplicacioninmartiu.inmart_logic.holders.UserAuth
import com.example.aplicacioninmartiu.inmart_logic.objects.Balanza
import com.example.aplicacioninmartiu.inmart_logic.objects.Producto
import com.example.aplicacioninmartiu.inmart_logic.objects.Usuario
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var progressDialog: ProgressDialog? = null

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

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val app: Inmart? = getObjectApp()
        val usuarioAuth: Usuario? = getUserAuth()

        val balanzasAsociadas = usuarioAuth?.obtenerBalanzasAsociadas()

        var btnRefrescarMediciones: ImageButton = binding.btnRefrescarPeso
        btnRefrescarMediciones.isEnabled = false

        btnRefrescarMediciones.setOnClickListener {
            showProgressDialog()
            app?.obtenerMedicionesSensoresPeso(usuarioAuth!!) { _ ->
                if (balanzasAsociadas!!.isNotEmpty()) {
                    for (balanza in balanzasAsociadas) {
                        if (balanza.obtenerId() == 1 && balanza.obtenerPesoInicial() != 0.0) {
                            println("PESO INICIAL PARA BALANZA 1 ${balanza.obtenerPesoInicial()}")

                            val pieChart: PieChart = binding.piechart
                            pieChart.clearChart()

                            if(balanza.obtenerPesoRestante() == 0.0) {
                                pieChart.addPieSlice(
                                    PieModel(
                                        "PESO INICIAL",
                                        balanza.obtenerPesoInicial().toFloat(),
                                        Color.parseColor("#000000")
                                    )
                                )
                            } else {
                                pieChart.addPieSlice(
                                    PieModel(
                                        "Consumido",
                                        balanza.obtenerPesoInicial().toFloat() - balanza.obtenerPesoRestante().toFloat(),
                                        Color.parseColor("#FFFFFF")
                                    )
                                )

                                pieChart.addPieSlice(
                                    PieModel(
                                        "Peso Real",
                                        balanza.obtenerPesoRestante().toFloat(),
                                        Color.parseColor("#000000")
                                    )
                                )
                            }
                        }
                        val estaPorAgotarse = balanza.chequearProductoPesado()
                        if(estaPorAgotarse) {
                            usuarioAuth.agregarProductoAgotado(balanza.obtenerProducto())
                        }
                    }
                    hideProgressDialog()
                }
            }
        }

        val btnAñadirPorPeso: Button = binding.btnAAdirPorPeso

        btnAñadirPorPeso.setOnClickListener { view: View? ->
            var cardView: CardView?
            var nombreProductoPeso: TextView?


            if(balanzasAsociadas?.size == 0) {
                cardView = binding.cardView
                nombreProductoPeso = binding.nombreProductoPeso
                println("CREANDO NOMBRE PROD. 1")
            } else {
                cardView = binding.cardView2
                nombreProductoPeso = binding.nombreProductoPeso2
                println("CREANDO NOMBRE PROD. 2")
            }

            println("CANTIDAD DE BOTONES EN LA LISTA DE PESO ${balanzasAsociadas?.size}")

            mostrarDialogoBalanzas(cardView, nombreProductoPeso, btnAñadirPorPeso, btnRefrescarMediciones,balanzasAsociadas!!)
        }

        val btnAñadirManual = binding.btnAAdirManual
        val containerProductos = binding.containerProductos

        btnAñadirManual.setOnClickListener {
            mostrarDialogoManual(containerProductos)
        }

        val btnEditar1 = binding.btnEditar
        btnEditar1.setOnClickListener {
            var nombreProductoPeso = binding.nombreProductoPeso
            actualizarDialogoBalanzas(nombreProductoPeso!!, 0)
            balanzasAsociadas!![0].registrarPesoInicial(0.0)
            balanzasAsociadas!![0].registrarPesoRestante(0.0)
            println("ACTUALIZANDO NOMBRE PROD. 1")
        }

        val btnRecomendaciones = binding.btnRecomendaciones
        btnRecomendaciones.setOnClickListener {
            val intentRecomendaciones = Intent(activity, Recomendaciones::class.java)
            intentRecomendaciones.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intentRecomendaciones)
        }

        println("ME CREASTEEEEEEEEEEEEEEEEEEEEE")


        if(balanzasAsociadas!!.isNotEmpty()) {
            btnRefrescarMediciones.isEnabled = true
            app?.activarMedicionesSensoresPeso(usuarioAuth!!) { _ ->
                var cardView: CardView = binding.cardView
                var nombreProductoPeso: TextView? = binding.nombreProductoPeso
                for (balanza in balanzasAsociadas) {

                    if (balanza.obtenerId() == 1 && balanza.obtenerPesoInicial() != 0.0) {
                        val pieChart: PieChart = binding.piechart
                        pieChart.clearChart()

                        if (balanza.obtenerPesoRestante() == 0.0 && balanza.obtenerPesoInicial() != 0.0) {
                            pieChart.addPieSlice(
                                PieModel(
                                    "PESO INICIAL",
                                    balanza.obtenerPesoInicial().toFloat(),
                                    Color.parseColor("#000000")
                                )
                            )
                        } else {
                            pieChart.addPieSlice(
                                PieModel(
                                    "Consumido",
                                    balanza.obtenerPesoInicial()
                                        .toFloat() - balanza.obtenerPesoRestante().toFloat(),
                                    Color.parseColor("#FFFFFF")
                                )
                            )

                            if (balanza.obtenerPesoInicial() != 0.0) {
                                pieChart.addPieSlice(
                                    PieModel(
                                        "Peso Real",
                                        balanza.obtenerPesoRestante().toFloat(),
                                        Color.parseColor("#000000")
                                    )
                                )
                            }

                        }
                        println("RECUPERANDO NOMBRE PROD. 1")
                    }

                    cardView.visibility = View.VISIBLE
                    nombreProductoPeso!!.text =
                        "${balanza.obtenerProducto().obtenerNombreProducto()}"
                    cardView.animate().alpha(1.0f)
                    btnAñadirPorPeso.animate().translationYBy(190f)
                }
            }
        }

        val listaManualAsociada = usuarioAuth!!.obtenerListaManual()

        if(listaManualAsociada!!.isNotEmpty()) {
            val containerProductos = binding.containerProductos
            for (producto in listaManualAsociada) {
                val nuevoBoton = Button(requireContext())
                nuevoBoton.text = producto.obtenerNombreProducto()

                nuevoBoton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                nuevoBoton.typeface = ResourcesCompat.getFont(
                    requireContext(),
                    com.example.aplicacioninmartiu.R.font.montserrat_light
                )
                nuevoBoton.textAlignment = View.TEXT_ALIGNMENT_CENTER
                nuevoBoton.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        com.example.aplicacioninmartiu.R.color.black
                    )
                )

                nuevoBoton.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                nuevoBoton.setOnClickListener {

                    // Acción al hacer clic en el botón (mostrar mensaje, etc.)
                    nuevoBoton.setOnClickListener {
                        nuevoBoton.visibility = View.GONE
                        usuarioAuth.agregarProductoAgotado(producto)
                        listaManualAsociada.remove(producto)
                    }
                }
                containerProductos.addView(nuevoBoton)
            }
        }




        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun actualizarDialogoBalanzas(nombreProductoPeso: TextView, id: Int) {
        val usuarioAuth: Usuario? = getUserAuth()

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Editar Producto Balanza")

        val input = EditText(requireContext())
        builder.setView(input)

        builder.setPositiveButton("Aceptar") { dialog, _ ->
            val nombreProducto = input.text.toString()
            val balanzaEncontrada = usuarioAuth!!.obtenerBalanzasAsociadas()[id]
            balanzaEncontrada.actualizarProductoPesado(Producto(nombreProducto))
            nombreProductoPeso.text = "$nombreProducto"
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun mostrarDialogoBalanzas(cardView: CardView, nombreProductoPeso: TextView, btnAñadirPorPeso: Button, btnRefrescarMediciones: ImageButton, balanzasAsociadas: MutableList<Balanza>) {
        val app: Inmart? = getObjectApp()
        val usuarioAuth: Usuario? = getUserAuth()

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Agregar Producto Balanza")


        val input = EditText(requireContext())
        builder.setView(input)

        builder.setPositiveButton("Aceptar") { dialog, _ ->
            val nombreProducto = input.text.toString()
            btnAñadirPorPeso.isEnabled = false
            btnRefrescarMediciones.isEnabled = true

            app?.registrarProductosBalanza(Producto(nombreProducto), usuarioAuth!!)
            app?.activarMedicionesSensoresPeso(usuarioAuth!!) { _ ->
                cardView.visibility = View.VISIBLE
                nombreProductoPeso.text = "$nombreProducto"
                cardView.animate().alpha(1.0f)
                btnAñadirPorPeso.animate().translationYBy(190f)
                dialog.dismiss()
            }
            if(balanzasAsociadas?.size != 1) {
                btnAñadirPorPeso.isEnabled = true
                println("REACTIVANDO BOTON")
            }
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun mostrarDialogoManual(containerProductos: LinearLayout) {
        val usuarioAuth: Usuario? = getUserAuth()

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Agregar Producto Manual")


        val input = EditText(requireContext())
        builder.setView(input)

        builder.setPositiveButton("Aceptar") { dialog, _ ->
            val nombreProducto = input.text.toString()
            val nuevoBoton = Button(requireContext())

            val nuevoProducto = Producto(nombreProducto)
            val listaManualAsociada = usuarioAuth!!.obtenerListaManual()
            usuarioAuth!!.añadirProductoListaManual(nuevoProducto)

            nuevoBoton.text = nombreProducto

            nuevoBoton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            nuevoBoton.typeface = ResourcesCompat.getFont(requireContext(), com.example.aplicacioninmartiu.R.font.montserrat_light)
            nuevoBoton.textAlignment = View.TEXT_ALIGNMENT_CENTER
            nuevoBoton.setTextColor(ContextCompat.getColor(requireContext(), com.example.aplicacioninmartiu.R.color.black))

            nuevoBoton.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            nuevoBoton.setOnClickListener {

                // Acción al hacer clic en el botón (mostrar mensaje, etc.)
                nuevoBoton.setOnClickListener {
                    nuevoBoton.visibility = View.GONE
                    usuarioAuth.agregarProductoAgotado(nuevoProducto)
                    listaManualAsociada.remove(nuevoProducto)
                }
            }
            // Agregar el nuevo botón al contenedor
            containerProductos.addView(nuevoBoton)
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun showProgressDialog() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog?.setMessage("Cargando...") // Puedes personalizar el mensaje según tus necesidades
        progressDialog?.setCancelable(false)
        progressDialog?.show()
    }

    private fun hideProgressDialog() {
        progressDialog?.dismiss()
        progressDialog = null
    }
}

