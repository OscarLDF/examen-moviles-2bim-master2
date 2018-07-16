package com.example.Oscar.examen_moviles_2bim.parser

import com.beust.klaxon.Klaxon
import com.example.Oscar.examen_moviles_2bim.objetos.Aplicacion
import com.example.Oscar.examen_moviles_2bim.objetos.Foto
import com.example.Oscar.examen_moviles_2bim.objetos.SistemaOperativo
import com.example.Oscar.examen_moviles_2bim.objetos.Usuario
import java.text.SimpleDateFormat

class JsonParser {
    companion object {
        private val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        private val klaxon = Klaxon()

        fun jsonToOSList(arrayOSjson: String): ArrayList<SistemaOperativo>? {
            val sistemasOperativos = klaxon.fieldConverter(KlaxonDate::class, DateFieldConverter).parseArray<SistemaOperativo>(arrayOSjson)
            return sistemasOperativos as ArrayList<SistemaOperativo>
        }

        fun usuarioToJson(usuario: Usuario): String {
            val usuarioJson = """{
                    "username":"${usuario.username}",
                    "password":"${usuario.password}"
                }""".trimIndent().trim()
            return usuarioJson
        }

        fun jsonToUsuario(usuarioJson: String): Usuario? {
            val usuario = klaxon.parse<Usuario>(usuarioJson)
            return usuario
        }

        fun osToJson(sistemaOperativo: SistemaOperativo): String {
            val sistemaJson = """{
                "nombre": "${sistemaOperativo.nombre}",
                "version": "${sistemaOperativo.version}",
                "fechaLanzamiento": "${dateFormat.format(sistemaOperativo.fechaLanzamiento)}",
                "pesoEnGigas": "${sistemaOperativo.pesoEnGigas}",
                "instalado": "${sistemaOperativo.instalado}"
                }""".trimIndent().trim()
            return sistemaJson
        }

        fun appToJson(aplicacion: Aplicacion): String {
            val aplicacionJson = """{
                "nombre":"${aplicacion.nombre}",
                "version":"${aplicacion.version}",
                "pesoEnGigas":"${aplicacion.pesoEnGigas}",
                "urlDescarga":"${aplicacion.urlDescarga}",
                "fechaLanzamiento":"${dateFormat.format(aplicacion.fechaLanzamiento)}",
                "costo": "${aplicacion.costo}",
                "sistemaOperativo": "${aplicacion.sistemaOperativo}"
            }""".trimIndent().trim()
            return aplicacionJson
        }

        fun jsonToApp(jsonApp: String): Aplicacion?{
            val aplicacion = klaxon.fieldConverter(KlaxonDate:: class, DateFieldConverter).parse<Aplicacion>(jsonApp)
            return aplicacion
        }

        fun jsonToAppList(arrayAppJson: String): ArrayList<Aplicacion>? {
            val aplicaciones = klaxon.fieldConverter(KlaxonDate::class, DateFieldConverter).parseArray<Aplicacion>(arrayAppJson)
            return aplicaciones as ArrayList<Aplicacion>
        }

        fun fotoToJson(foto: Foto): String{
            val fotoJson = """{
                    "datos": "${foto.datos}",
                    "extension": "${foto.extension}",
                    "aplicacion": "${foto.aplicacion}"
                }""".trimIndent().trim()
            return fotoJson
        }

        fun jsonToFoto(jsonFoto: String): Foto?{
            val foto = klaxon.parse<Foto>(jsonFoto)
            return foto
        }


    }
}