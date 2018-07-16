package com.example.Oscar.examen_moviles_2bim.request

import android.util.Log
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.httpDelete
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.httpPut
import com.github.kittinunf.result.Result

class HttpRequest {

    companion object {
        private val ip = "192.168.100.48"
        private val puerto = "1337"
        private val uriBase = "http://$ip:$puerto"

        fun requestHTTP(metodo: String, ruta: String, datosJson: String = "", callback: (error: Boolean, datos: String) -> Any?){
            var request: Request

            when(metodo.toUpperCase()){
                "GET" -> {
                    request = "$uriBase$ruta".httpGet()
                    makeRequest(request, callback)
                }
                "POST" -> {
                    request = "$uriBase$ruta".httpPost()
                    request.header(Pair("CONTENT-TYPE","application/json"))
                    request.body(datosJson)
                    makeRequest(request, callback)
                }
                "PUT" -> {
                    request = "$uriBase$ruta".httpPut()
                    request.header(Pair("CONTENT-TYPE","application/json"))
                    request.body(datosJson)
                    makeRequest(request, callback)
                }
                "DELETE" -> {
                    request = "$uriBase$ruta".httpDelete()
                    makeRequest(request, callback)
                }
            }
        }

        private fun makeRequest(request: Request, callback: (error: Boolean, datos: String) -> Any?){
            var datos = ""
            request.responseString{ request, response, result ->
                when(result){
                    is Result.Failure -> {
                        Log.i("HTTP_FUEL", "${result.getException()} - ${result.error.response}")
                        callback(true, datos)
                    }
                    is Result.Success -> {
                        Log.i("HTTP_FUEL", "${result.get()}")
                        datos = result.get()
                        callback(false, datos)
                    }
                }
            }
        }
    }
}