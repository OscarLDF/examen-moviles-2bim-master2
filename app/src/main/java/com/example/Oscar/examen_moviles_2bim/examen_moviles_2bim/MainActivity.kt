package com.example.Oscar.examen_moviles_2bim

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.Oscar.examen_moviles_2bim.objetos.Usuario
import com.example.Oscar.examen_moviles_2bim.parser.JsonParser
import com.example.Oscar.examen_moviles_2bim.request.HttpRequest
import com.example.Oscar.examen_moviles_2bim.vendedor.VendedorMainActivity
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_login.setOnClickListener{ v: View? ->
            val username = editText_login_usuario.text.toString()
            val password = editText_login_password.text.toString()
            val usuario: Usuario? = Usuario(username = username, password = password)
            val usuarioJson = JsonParser.usuarioToJson(usuario as Usuario)

            HttpRequest.requestHTTP("POST", "/usuario/login", usuarioJson,callback = { error, datos ->
                if(error){
                    Toast.makeText(this, "Inicio de sessión fallido", Toast.LENGTH_LONG).show()
                }else{
                    async(UI){
                        val usuarioDef: Deferred<Usuario?> = bg{
                            JsonParser.jsonToUsuario(datos)
                        }
                        when(usuarioDef.await()!!.tipo){
                            "VENDEDOR" -> irActividadVendedor()
                            "COMPRADOR" -> irActividadComprador()
                            "DELIVERY" -> irActividadDelivery()
                        }
                    }
                }
            })
        }
    }


    fun irActividadVendedor(){
        val intent = Intent(this, VendedorMainActivity:: class.java)
        startActivity(intent)
    }

    fun irActividadComprador(){

    }
    fun irActividadDelivery(){

    }

}
