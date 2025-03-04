package com.example.aplicacioninmartiu.inmart_logic.objects

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class Usuario (
    private val nombre: String,
    private val correo: String,
    private val contrasena: String,
    private val direccion: String,
    private val telefono: String
)
{
    private val balanzasAsociadas: MutableList<Balanza> = mutableListOf()
    private val productosListaManual: MutableList<Producto> = mutableListOf()
    private val recetasAsociadas: MutableList<Receta> = mutableListOf()
    private val productosSupermercadoAsociados: MutableMap<String, MutableList<Producto>> = mutableMapOf()
    private val estadisticasAmbientalesRegistradas = mutableListOf<Ambiente>()
    private val productosAgotados: MutableList<Producto> = mutableListOf()
    private val carritoMercado: MutableList<CarroMercado> = mutableListOf()
    private val idsMercadosCreados = mutableListOf<Int>()


    companion object {
        //Contador de tipo Static para incrementar el Id y asegurar que sea único para cada Usuario creado
        private var contador: Int = 0
    }

    //Atributo interno
    private val id: Int

    //Constructor complementario que instancia el Id en el objeto
    init {
        contador++
        this.id = contador
    }

    //Getters para los atributos del usuario
    fun obtenerId(): Int {
        return this.id
    }

    fun obtenerNombre(): String {
        return this.nombre
    }

    fun obtenerCorreo(): String {
        return this.correo
    }

    fun obtenerContrasena(): String {
        return this.contrasena
    }

    fun obtenerDireccion(): String {
        return this.direccion
    }

    fun obtenerTelefono(): String {
        return this.telefono
    }

    fun asociarBalanza(balanza: Balanza) {
        this.balanzasAsociadas.add(balanza)
    }

    fun obtenerBalanzasAsociadas(): MutableList<Balanza> {
        return this.balanzasAsociadas
    }

    fun obtenerRecetasAsociadas(): MutableList<Receta> {
        return this.recetasAsociadas
    }

    fun asociarReceta(receta: Receta) {
        this.recetasAsociadas.add(receta)
    }

    fun obtenerProductosSupermercadosAsociados(): MutableMap<String, MutableList<Producto>> {
        return this.productosSupermercadoAsociados
    }

    fun asociarProductoSupermercado(nombreSupermecado: String, producto: MutableList<Producto>) {
        this.productosSupermercadoAsociados[nombreSupermecado] = producto
    }

    fun obtenerEstadisticasAmbientalesAsociadas(): MutableList<Ambiente> {
        return this.estadisticasAmbientalesRegistradas
    }

    fun añadirEstadisticaAmbiental(ambiente: Ambiente) {
        this.estadisticasAmbientalesRegistradas.add(ambiente)
    }

    fun obtenerListaManual(): MutableList<Producto> {
        return this.productosListaManual
    }

    fun añadirProductoListaManual(producto: Producto) {
        this.productosListaManual.add(producto)
    }

    fun obtenerProductosAgotados(): MutableList<Producto> {
        return this.productosAgotados
    }

    fun agregarProductoAgotado(producto: Producto) {
        this.productosAgotados.add(producto)
    }

    fun agregarAlCarrito(producto: Producto, nombreSupermercado: String) {
        this.carritoMercado.add(CarroMercado(producto, nombreSupermercado))
    }

    fun obtenerCarritoMercado(): MutableList<CarroMercado> {
        return this.carritoMercado
    }

    fun añadirSupermercado(id: Int) {
        this.idsMercadosCreados.add(id)
    }

    fun obtenerSupermercados(): MutableList<Int> {
        return this.idsMercadosCreados
    }

}