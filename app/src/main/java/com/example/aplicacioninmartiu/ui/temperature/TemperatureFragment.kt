package com.example.aplicacioninmartiu.ui.temperature

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.aplicacioninmartiu.R
import com.example.aplicacioninmartiu.databinding.FragmentTemperatureBinding
import com.example.aplicacioninmartiu.inmart_logic.Inmart
import com.example.aplicacioninmartiu.inmart_logic.holders.AppObject
import com.example.aplicacioninmartiu.inmart_logic.holders.UserAuth
import com.example.aplicacioninmartiu.inmart_logic.objects.Usuario

class TemperatureFragment : Fragment() {

    private var _binding: FragmentTemperatureBinding? = null
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
    ): View? {

        _binding =  FragmentTemperatureBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val app: Inmart? = getObjectApp()
        val usuarioAuth: Usuario? = getUserAuth()

        var btnRefrescarMediciones: ImageButton = binding.btnRefrescarTemp
        var txtViewIndiceTemp = binding.txtViewIndiceTemperatura
        var txtViewIndiceHum = binding.txtViewIndiceHumedad
        var txtViewGas = binding.txtIndiceGas

        var imgTemperatura = binding.imageView
        var imgGas = binding.imageView2


        btnRefrescarMediciones.setOnClickListener {
            showProgressDialog()
            app!!.activarMedicionesSensoresAmbiente(usuarioAuth!!) {
                app!!.obtenerMedicionesSensoresAmbiente(usuarioAuth!!) {
                    var listaMedicionesEncontrada = usuarioAuth.obtenerEstadisticasAmbientalesAsociadas()
                    var ultimaMedicion = listaMedicionesEncontrada[listaMedicionesEncontrada.size - 1]

                    if(ultimaMedicion.obtenerTemperatura() < 20) {
                        imgTemperatura.setImageResource(R.drawable.frio)
                    } else {
                        imgTemperatura.setImageResource(R.drawable.fuego)
                    }

                    if(ultimaMedicion.obtenerGasMetano() > 500) {
                        imgGas.setImageResource(R.drawable.unsafe)
                    } else {
                        imgGas.setImageResource(R.drawable.safe)
                    }

                    txtViewIndiceTemp.text = "${ultimaMedicion.obtenerTemperatura()} °C"
                    txtViewIndiceHum.text = "${ultimaMedicion.obtenerHumedad()}%"
                    txtViewGas.text = "${ultimaMedicion.obtenerGasMetano()} ppm"
                    hideProgressDialog()
                }
            }
        }

        return root
    }

    override fun onResume() {
        super.onResume()

        val usuarioAuth: Usuario? = getUserAuth()

        var txtViewIndiceTemp = binding.txtViewIndiceTemperatura
        var txtViewIndiceHum = binding.txtViewIndiceHumedad
        var txtViewIndiceGas = binding.txtIndiceGas
        var imgTemperatura = binding.imageView
        var imgGas = binding.imageView2

        var listaMedicionesEncontrada = usuarioAuth!!.obtenerEstadisticasAmbientalesAsociadas()

        if(listaMedicionesEncontrada.isNotEmpty()) {
            var ultimaMedicion = listaMedicionesEncontrada[listaMedicionesEncontrada.size - 1]

            if(ultimaMedicion.obtenerTemperatura() < 20) {
                imgTemperatura.setImageResource(R.drawable.frio)
            } else {
                imgTemperatura.setImageResource(R.drawable.fuego)
            }

            if(ultimaMedicion.obtenerGasMetano() > 500) {
                imgGas.setImageResource(R.drawable.unsafe)
            } else {
                imgGas.setImageResource(R.drawable.safe)
            }

            txtViewIndiceTemp.text = "${ultimaMedicion.obtenerTemperatura()} °C"
            txtViewIndiceHum.text = "${ultimaMedicion.obtenerHumedad()}%"
            txtViewIndiceGas.text = "${ultimaMedicion.obtenerGasMetano()}"
        }
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