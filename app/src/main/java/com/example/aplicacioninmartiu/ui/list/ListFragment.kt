package com.example.aplicacioninmartiu.ui.list

import android.app.ProgressDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.aplicacioninmartiu.R
import com.example.aplicacioninmartiu.databinding.FragmentListBinding
import com.example.aplicacioninmartiu.inmart_logic.Inmart
import com.example.aplicacioninmartiu.inmart_logic.external_connections.DominioSupermercado
import com.example.aplicacioninmartiu.inmart_logic.holders.AppObject
import com.example.aplicacioninmartiu.inmart_logic.holders.UserAuth
import com.example.aplicacioninmartiu.inmart_logic.objects.Producto
import com.example.aplicacioninmartiu.inmart_logic.objects.Supermercado
import com.example.aplicacioninmartiu.inmart_logic.objects.Usuario
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private var progressDialog: ProgressDialog? = null

    fun getUserAuth(): Usuario? {
        return UserAuth.getUser()
    }
    fun getObjectApp(): Inmart? {
        return AppObject.getApp()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListBinding.inflate(inflater, container, false)

        val app: Inmart? = getObjectApp()
        val usuarioAuth: Usuario? = getUserAuth()

        val root: View = binding.root

        val containerList = binding.containerList
        val imgBannerSupermercado = binding.imageView3
        imgBannerSupermercado.setImageResource(R.drawable.ad)

        val cardViewProducto = binding.cardView7
        val listViewProducto = binding.listView
        var arrayAdapter: ArrayAdapter<*>


        val listaProductosAgotados = usuarioAuth!!.obtenerProductosAgotados()


        val btnCerrar = binding.btnCerrar

        btnCerrar.setOnClickListener {
            cardViewProducto.visibility = View.INVISIBLE
        }

        val btnExito: ImageButton = binding.btnExito
        val btnCarulla: ImageButton = binding.btnCarulla
        val btnColsubsidio: ImageButton = binding.btnColsubsidio
        val btnJumbo: ImageButton = binding.btnJumbo


        for (producto in listaProductosAgotados) {
            val nuevoBoton = Button(requireContext())
            nuevoBoton.text = producto.obtenerNombreProducto()
            nuevoBoton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            nuevoBoton.typeface = ResourcesCompat.getFont(requireContext(), R.font.montserrat_light)
            nuevoBoton.textAlignment = View.TEXT_ALIGNMENT_CENTER
            nuevoBoton.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

            val anchoEnPixeles = 850
            val layoutParams = LinearLayout.LayoutParams(anchoEnPixeles, LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.gravity = Gravity.CENTER
            nuevoBoton.layoutParams = layoutParams



            nuevoBoton.setOnClickListener {
                cardViewProducto.visibility = View.VISIBLE
                val supermercadoExito = Supermercado(DominioSupermercado.EXITO.getDominio())
                btnExito.visibility = View.VISIBLE
                btnExito.setOnClickListener {
                    showProgressDialog()
                    listViewProducto.visibility = View.INVISIBLE
                    btnExito.alpha = 0.5f
                    btnCarulla.alpha = 1.0f
                    btnColsubsidio.alpha = 1.0f
                    btnJumbo.alpha = 1.0f

                    println("Filtrando precios de EXITO para el producto ${producto.obtenerNombreProducto()}")
                    app!!.obtenerPreciosSupermercado(usuarioAuth, supermercadoExito, producto.obtenerNombreProducto()) {
                        val diccionarioProductos =
                            usuarioAuth.obtenerProductosSupermercadosAsociados()

                        listViewProducto.visibility = View.VISIBLE

                        for ((supermercado, productos) in diccionarioProductos) {
                            if (supermercado == supermercadoExito.obtenerNombreAlmacen()) {
                                arrayAdapter = ArrayAdapter(
                                    requireContext(),
                                    android.R.layout.simple_list_item_1,
                                    productos
                                )
                                listViewProducto.adapter = arrayAdapter
                                listViewProducto.setOnItemClickListener { parent, view, position, id ->
                                    val productoSeleccionado = productos[position]
                                    usuarioAuth.agregarAlCarrito(productoSeleccionado, "Exito")
                                    println("SU CARRITO SE ENCUENTRA ASI: ${usuarioAuth.obtenerCarritoMercado()}")
                                    nuevoBoton.visibility = View.GONE
                                    listViewProducto.visibility = View.GONE
                                    cardViewProducto.visibility = View.GONE
                                    listaProductosAgotados.remove(producto)
                                    btnExito.alpha = 1.0f
                                    btnCarulla.alpha = 1.0f
                                    btnColsubsidio.alpha = 1.0f
                                    btnJumbo.alpha = 1.0f
                                }
                            }
                        }
                        hideProgressDialog()
                    }
                }

                val supermercadoCarulla = Supermercado(DominioSupermercado.CARULLA.getDominio())
                btnCarulla.visibility = View.VISIBLE
                btnCarulla.setOnClickListener {
                    showProgressDialog()
                    listViewProducto.visibility = View.INVISIBLE
                    btnExito.alpha = 1.0f
                    btnCarulla.alpha = 0.5f
                    btnColsubsidio.alpha = 1.0f
                    btnJumbo.alpha = 1.0f

                    println("Filtrando precios de CARULLA para el producto ${producto.obtenerNombreProducto()}")
                    app!!.obtenerPreciosSupermercado(usuarioAuth, supermercadoCarulla, producto.obtenerNombreProducto()) {
                        val diccionarioProductos =
                            usuarioAuth.obtenerProductosSupermercadosAsociados()

                        listViewProducto.visibility = View.VISIBLE

                        for ((supermercado, productos) in diccionarioProductos) {
                            if (supermercado == supermercadoCarulla.obtenerNombreAlmacen()) {
                                arrayAdapter = ArrayAdapter(
                                    requireContext(),
                                    android.R.layout.simple_list_item_1,
                                    productos
                                )
                                listViewProducto.adapter = arrayAdapter
                                listViewProducto.setOnItemClickListener { parent, view, position, id ->
                                    val productoSeleccionado = productos[position]
                                    usuarioAuth.agregarAlCarrito(productoSeleccionado, "Carulla")
                                    println("SU CARRITO SE ENCUENTRA ASI: ${usuarioAuth.obtenerCarritoMercado()}")
                                    nuevoBoton.visibility = View.GONE
                                    listViewProducto.visibility = View.GONE
                                    cardViewProducto.visibility = View.GONE
                                    listaProductosAgotados.remove(producto)
                                    btnExito.alpha = 1.0f
                                    btnCarulla.alpha = 1.0f
                                    btnColsubsidio.alpha = 1.0f
                                    btnJumbo.alpha = 1.0f
                                }
                            }
                        }
                        hideProgressDialog()
                    }
                }

                val supermercadoColsubsidio =
                    Supermercado(DominioSupermercado.COLSUBSIDIO.getDominio())
                btnColsubsidio.visibility = View.VISIBLE
                btnColsubsidio.setOnClickListener {
                    showProgressDialog()
                    listViewProducto.visibility = View.INVISIBLE
                    btnExito.alpha = 1.0f
                    btnCarulla.alpha = 1.0f
                    btnColsubsidio.alpha = 0.5f
                    btnJumbo.alpha = 1.0f

                    println("Filtrando precios de COLSUBSIDIO para el producto ${producto.obtenerNombreProducto()}")
                    app!!.obtenerPreciosSupermercado(usuarioAuth, supermercadoColsubsidio, producto.obtenerNombreProducto()) {
                        val diccionarioProductos =
                            usuarioAuth.obtenerProductosSupermercadosAsociados()

                        listViewProducto.visibility = View.VISIBLE

                        for ((supermercado, productos) in diccionarioProductos) {
                            if (supermercado == supermercadoColsubsidio.obtenerNombreAlmacen()) {
                                arrayAdapter = ArrayAdapter(
                                    requireContext(),
                                    android.R.layout.simple_list_item_1,
                                    productos
                                )
                                listViewProducto.adapter = arrayAdapter
                                listViewProducto.setOnItemClickListener { parent, view, position, id ->
                                    val productoSeleccionado = productos[position]
                                    usuarioAuth.agregarAlCarrito(productoSeleccionado, "Colsubsidio")
                                    println("SU CARRITO SE ENCUENTRA ASI: ${usuarioAuth.obtenerCarritoMercado()}")
                                    nuevoBoton.visibility = View.GONE
                                    listViewProducto.visibility = View.GONE
                                    cardViewProducto.visibility = View.GONE
                                    listaProductosAgotados.remove(producto)
                                    btnExito.alpha = 1.0f
                                    btnCarulla.alpha = 1.0f
                                    btnColsubsidio.alpha = 1.0f
                                    btnJumbo.alpha = 1.0f
                                }
                            }
                        }
                        hideProgressDialog()
                    }
                }

                val supermercadoJumbo = Supermercado(DominioSupermercado.JUMBO.getDominio())
                btnJumbo.visibility = View.VISIBLE
                btnJumbo.setOnClickListener {
                    showProgressDialog()
                    listViewProducto.visibility = View.INVISIBLE
                    btnExito.alpha = 1.0f
                    btnCarulla.alpha = 1.0f
                    btnColsubsidio.alpha = 1.0f
                    btnJumbo.alpha = 0.5f

                    println("Filtrando precios de JUMBO para el producto ${producto.obtenerNombreProducto()}")
                    app!!.obtenerPreciosSupermercado(usuarioAuth, supermercadoJumbo, producto.obtenerNombreProducto()) {
                        val diccionarioProductos =
                            usuarioAuth.obtenerProductosSupermercadosAsociados()

                        listViewProducto.visibility = View.VISIBLE

                        for ((supermercado, productos) in diccionarioProductos) {
                            if (supermercado == supermercadoJumbo.obtenerNombreAlmacen()) {
                                arrayAdapter = ArrayAdapter(
                                    requireContext(),
                                    android.R.layout.simple_list_item_1,
                                    productos
                                )
                                listViewProducto.adapter = arrayAdapter
                                listViewProducto.setOnItemClickListener { parent, view, position, id ->
                                    val productoSeleccionado = productos[position]
                                    usuarioAuth.agregarAlCarrito(productoSeleccionado, "Jumbo")
                                    println("SU CARRITO SE ENCUENTRA ASI: ${usuarioAuth.obtenerCarritoMercado()}")
                                    nuevoBoton.visibility = View.GONE
                                    listViewProducto.visibility = View.GONE
                                    cardViewProducto.visibility = View.GONE
                                    listaProductosAgotados.remove(producto)
                                    btnExito.alpha = 1.0f
                                    btnCarulla.alpha = 1.0f
                                    btnColsubsidio.alpha = 1.0f
                                    btnJumbo.alpha = 1.0f
                                }
                            }
                        }
                        hideProgressDialog()
                    }
                }

            }
            containerList.addView(nuevoBoton)
        }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
