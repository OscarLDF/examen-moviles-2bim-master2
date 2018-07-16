package com.example.Oscar.examen_moviles_2bim.vendedor

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.Oscar.examen_moviles_2bim.R
import kotlinx.android.synthetic.main.activity_vendedor_main.*

class VendedorMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendedor_main)

        btn_crear_os.setOnClickListener{
            irActividadCrearOS()
        }

        btn_listar_os.setOnClickListener{
            irActividadListarOS()
        }

    }

    fun irActividadCrearOS(){
        val intent = Intent(this, CreateOSActivity::class.java)
        startActivity(intent)
    }

    fun irActividadListarOS(){
        val intent = Intent(this, OSListActivity::class.java)
        startActivity(intent)
    }


}
