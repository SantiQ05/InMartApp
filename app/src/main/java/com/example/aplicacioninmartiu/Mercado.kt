package com.example.aplicacioninmartiu

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.aplicacioninmartiu.inmart_logic.Inmart
import com.example.aplicacioninmartiu.inmart_logic.holders.AppObject
import com.example.aplicacioninmartiu.inmart_logic.holders.UserAuth
import com.example.aplicacioninmartiu.inmart_logic.objects.Usuario
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class Mercado : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private var supermercadoLocalizado: String? = ""

    fun getObjectApp(): Inmart? {
        return AppObject.getApp()
    }

    fun getUserAuth(): Usuario? {
        return UserAuth.getUser()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mercado)

        val supermercado = intent.getStringExtra("strSupermercado")
        this.supermercadoLocalizado = supermercado

        val bannerSupermercado: ImageView = findViewById(R.id.imageView6)

        when (supermercado) {
            "Exito" -> bannerSupermercado.setImageResource(R.drawable.exitoimage)
            "Carulla" -> bannerSupermercado.setImageResource(R.drawable.carullaimage)
            "Colsubsidio" -> bannerSupermercado.setImageResource(R.drawable.colsubimage)
            "Jumbo" -> bannerSupermercado.setImageResource(R.drawable.jumboimage)
        }


        val app: Inmart? = getObjectApp()
        val usuarioAuth: Usuario? = getUserAuth()

        val obtenerCarritoMercadoAsociado = usuarioAuth!!.obtenerCarritoMercado()

        //val checkBox: CheckBox = findViewById(R.id.productoList)
        val precioTotalCarrito: TextView = findViewById(R.id.totalList)

        val linearLayout = findViewById<LinearLayout>(R.id.contenedorCarritoMercado)

        val listaProductosTotalizados = mutableListOf<Double>()
        linearLayout.orientation = LinearLayout.VERTICAL  // Establecer orientación vertical

        for (elemento in obtenerCarritoMercadoAsociado) {
            if (elemento.obtenerSupermercadoCarrito() == supermercado) {
                val checkBox = CheckBox(this)
                checkBox.text = elemento.obtenerProductoCarrito().obtenerNombreProducto()

                val precioText = TextView(this)
                precioText.text = elemento.obtenerProductoCarrito().obtenerPrecioProducto().toString()

                val verticalLayout = LinearLayout(this)
                verticalLayout.orientation = LinearLayout.VERTICAL

                checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        listaProductosTotalizados.add(elemento.obtenerProductoCarrito().obtenerPrecioProducto())
                    } else {
                        listaProductosTotalizados.remove(elemento.obtenerProductoCarrito().obtenerPrecioProducto())
                    }

                    val totalCarrito = app!!.calcularTotalCarrito(usuarioAuth, listaProductosTotalizados)
                    precioTotalCarrito.text = totalCarrito.toString()
                }

                verticalLayout.addView(checkBox)
                verticalLayout.addView(precioText)

                linearLayout.addView(verticalLayout)
            }
        }

        val btnLimpiarMercado: Button = findViewById(R.id.btnAcabarMercado)
        val supermercadosCreados = usuarioAuth!!.obtenerSupermercados()

        btnLimpiarMercado.setOnClickListener {
            if(obtenerCarritoMercadoAsociado.isNotEmpty()) {
                val iterator = obtenerCarritoMercadoAsociado.iterator()
                while (iterator.hasNext()) {
                    val elemento = iterator.next()
                    if (elemento.obtenerSupermercadoCarrito() == supermercado) {
                        iterator.remove()
                        println("LIMPIANDO MERCADOOOOOOOOOOOOOOOOOOOOOOOO")
                    }
                }

                when(supermercado) {
                    "Exito" -> supermercadosCreados.remove(R.id.boton_exito)
                    "Carulla" -> supermercadosCreados.remove(R.id.boton_carulla)
                    "Colsubsidio" -> supermercadosCreados.remove(R.id.boton_colsubsidio)
                    "Jumbo" -> supermercadosCreados.remove(R.id.boton_jumbo)
                }

                val intentHome: Intent = Intent(this, PaginaHome::class.java)
                startActivity(intentHome)
            }
        }

        val btnUbicacionSupermercado: Button = findViewById(R.id.btnMap)

        val cardViewMap: CardView = findViewById(R.id.cardViewMap)
        btnUbicacionSupermercado.setOnClickListener {
            cardViewMap.visibility = View.VISIBLE
            createMapFragment()
        }

        val btnCerrarMapa: ImageButton = findViewById(R.id.btnCerrar)
        btnCerrarMapa.setOnClickListener {
            cardViewMap.visibility = View.INVISIBLE
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        createMarker(this.supermercadoLocalizado!!)
    }

    private fun createMarker(supermercado: String) {

        val supermarketPlaces = mutableListOf<LatLng>()

        supermarketPlaces.add(LatLng(4.663174918787488, -74.05514656594613))

        if(supermercado == "Carulla") {
            supermarketPlaces.add(LatLng(4.6612870855863635, -74.05625915210189))
            supermarketPlaces.add(LatLng(4.666782583787594, -74.05005910649652))
            supermarketPlaces.add(LatLng(4.669595718807308, -74.05580685849772))
        } else if(supermercado == "Exito") {
            supermarketPlaces.add(LatLng(4.662683134182845, -74.05865661289529))
        } else if(supermercado == "Colsubsidio") {
            supermarketPlaces.add(LatLng(4.69683222350486, -74.04546497774156))
        } else if(supermercado == "Jumbo") {
            supermarketPlaces.add(LatLng(4.665805361347097, -74.05566163033221))
        }

        var esOrigen = true

        for (place in supermarketPlaces) {
            if(esOrigen) {
                map.addMarker(MarkerOptions().position(place).title("Estas aquí"))
                esOrigen = false
            } else {
                map.addMarker(MarkerOptions().position(place).title("$supermercado cercano a tu ubicación"))
            }
        }

        val builder = LatLngBounds.Builder()
        for (place in supermarketPlaces) {
            builder.include(place)
        }

        val bounds = builder.build()
        val padding = 100
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        map.animateCamera(cameraUpdate)

    }

    private fun createMapFragment() {
        val mapFragmentTag = "MAP_FRAGMENT_TAG"
        var mapFragment = supportFragmentManager.findFragmentByTag(mapFragmentTag) as SupportMapFragment?

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentMapContainer, mapFragment, mapFragmentTag)
                .commit()
            supportFragmentManager.executePendingTransactions()
        }

        mapFragment!!.getMapAsync(this)
    }

}


