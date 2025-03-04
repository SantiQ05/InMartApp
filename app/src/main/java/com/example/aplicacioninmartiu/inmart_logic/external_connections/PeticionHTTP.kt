package com.example.aplicacioninmartiu.inmart_logic.external_connections

/* Librerias para manejo de Peticiones HTTP */

import android.os.AsyncTask
import com.example.aplicacioninmartiu.inmart_logic.Inmart
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URI

class PeticionHTTP {

    class AsyncPeticionGET (
        private val listener: PeticionHTTPListener
    ) : AsyncTask<String, Void, Int>() {
        override fun doInBackground(vararg params: String): Int {
            val urlBase = params[0]
            val endpoint = params[1]

            try {
                // Código de tu métodoGET
                val uriPeticion = URI("${urlBase}/${endpoint}")
                val urlPeticion = uriPeticion.toURL()
                val connection = urlPeticion.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                return connection.responseCode

            } catch (ex: Exception) {
                ex.printStackTrace()
                return -1
            }
        }

        override fun onPostExecute(result: Int) {
            super.onPostExecute(result)

            // Maneja el resultado después de que doInBackground haya terminado
            if (result == 200) {
                println("Peticion HTTP GET Procesada correctamente.")
                listener.peticionCompleta(result.toString())
            } else {
                println("Peticion HTTP GET recibió un error del servidor!")
            }
        }
    }

    class AsyncPeticionPOST (
        private val listener: PeticionHTTPListener
    ) : AsyncTask<String, Void, String?>() {
        override fun doInBackground(vararg params: String): String? {
            val urlBase = params[0]
            val endpoint = params[1]
            val requestBody = params[2]

            try {
                // Creamos la URI que identifica al servidor al cual se hace la petición
                val uriPeticion = URI("${urlBase}/${endpoint}")

                // Creamos la URL para interactuar con el recurso URI a través de peticiones HTTP
                val urlPeticion = uriPeticion.toURL()

                // Creamos la conexión HTTP
                val connection = urlPeticion.openConnection() as HttpURLConnection

                // Configuramos el método de la petición
                connection.requestMethod = "POST"
                // Indicamos que la cabecera contendrá un archivo JSON para hacer la petición al servidor
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                // Creamos el output para enviar los datos JSON
                val outputStream = DataOutputStream(connection.outputStream)
                outputStream.writeBytes(requestBody)
                outputStream.flush()
                outputStream.close()

                // Obtiene el código de respuesta de la petición
                val responseCode = connection.responseCode

                // Leemos la información recibida por el servidor
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = reader.readText()
                reader.close()

                // Retornamos la respuesta y el código si es exitosa
                return if(responseCode == 200) response else null

            } catch (ex: Exception) {
                // Manejamos excepciones si son generadas e imprimimos las excepciones
                ex.printStackTrace()
                return null
            }
        }

        override fun onPostExecute(response: String?) {
            super.onPostExecute(response)
            // Maneja el resultado después de que doInBackground haya terminado
            if (response != null) {
                println("Peticion HTTP POST Procesada correctamente. Response: $response")
                listener.peticionCompleta(response)
            } else {
                println("Peticion HTTP POST recibió un error del servidor!")
                listener.peticionCompleta(null)
            }
        }
    }

}