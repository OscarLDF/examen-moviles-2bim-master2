package com.example.Oscar.examen_moviles_2bim.vendedor

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.Oscar.examen_moviles_2bim.R
import com.example.Oscar.examen_moviles_2bim.objetos.SistemaOperativo
import com.example.Oscar.examen_moviles_2bim.parser.JsonParser
import com.example.Oscar.examen_moviles_2bim.request.HttpRequest
import kotlinx.android.synthetic.main.activity_create_os.*
import java.text.SimpleDateFormat

class CreateOSActivity : AppCompatActivity() {

    private var sistemaOperativo: SistemaOperativo? = null
    private val dateFormat = SimpleDateFormat("dd-MM-yyyy")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_os)

        sistemaOperativo = intent.getParcelableExtra("OS")

        sistemaOperativo?.let {
            llenarFormulario(sistemaOperativo as SistemaOperativo)
        }

        btn_guardar_os.setOnClickListener{

            if(sistemaOperativo == null) {
                val sistema = crearSistemaOperativo()
                val osJson = JsonParser.osToJson(sistema)

                HttpRequest.requestHTTP("POST", "/SistemaOperativo", osJson, { error, datos ->
                    if (error) {
                        Toast.makeText(this, "Error al crear sistema operativo", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Sistema Operativo creado", Toast.LENGTH_LONG).show()
                    }

                })
            }else{
                actualizarSistema(sistemaOperativo as SistemaOperativo)
                val osJson = JsonParser.osToJson(sistemaOperativo as SistemaOperativo)

                HttpRequest.requestHTTP("PUT", "/SistemaOperativo/${sistemaOperativo!!.id}", osJson, { error, datos ->
                    if (error) {
                        Toast.makeText(this, "Error al actualizar", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_LONG).show()
                    }

                })
            }

        }
    }

    fun actualizarSistema(sistemaOperativo: SistemaOperativo){
        val nombre = editText_nombre_os.text.toString()
        val version = editText_version_os.text.toString().toInt()
        val fechaLanzamiento = dateFormat.parse(editText_fecha_os.text.toString())
        val peso = editText_peso_os.text.toString().toDouble()
        val instalado = radio_yes_os.isChecked

        sistemaOperativo.nombre = nombre
        sistemaOperativo.version = version
        sistemaOperativo.fechaLanzamiento = fechaLanzamiento
        sistemaOperativo.pesoEnGigas = peso
        sistemaOperativo.instalado = instalado
    }

    fun llenarFormulario(sistemaOperativo: SistemaOperativo){
        editText_nombre_os.append(sistemaOperativo.nombre)
        editText_fecha_os.append(dateFormat.format(sistemaOperativo.fechaLanzamiento))
        editText_version_os.append(sistemaOperativo.version.toString())
        editText_peso_os.append(sistemaOperativo.pesoEnGigas.toString())
        if(sistemaOperativo.instalado) radioGroup_yes_no.check(R.id.radio_yes_os) else radioGroup_yes_no.check(R.id.radio_no_os)
    }

    fun crearSistemaOperativo(): SistemaOperativo {
        val nombre = editText_nombre_os.text.toString()
        val version = editText_version_os.text.toString().toInt()
        val fechaLanzamiento = dateFormat.parse(editText_fecha_os.text.toString())
        val peso = editText_peso_os.text.toString().toDouble()
        val instalado = radio_yes_os.isChecked

        val sistemaOperativo = SistemaOperativo(
                nombre = nombre,
                version = version,
                fechaLanzamiento = fechaLanzamiento,
                pesoEnGigas = peso,
                instalado = instalado)

        return sistemaOperativo

    }


}
