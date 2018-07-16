package com.example.Oscar.examen_moviles_2bim.vendedor

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.example.Oscar.examen_moviles_2bim.R
import com.example.Oscar.examen_moviles_2bim.adaptadores.AppAdapter
import com.example.Oscar.examen_moviles_2bim.objetos.Aplicacion
import com.example.Oscar.examen_moviles_2bim.objetos.SistemaOperativo
import com.example.Oscar.examen_moviles_2bim.parser.JsonParser
import com.example.Oscar.examen_moviles_2bim.request.HttpRequest
import kotlinx.android.synthetic.main.activity_create_os.*
import kotlinx.android.synthetic.main.activity_osdetail.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import java.text.SimpleDateFormat

class OSDetailActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var sistemaOperativo: SistemaOperativo? = null
    var aplicaciones: ArrayList<Aplicacion>? = ArrayList()
    private val dateFormat = SimpleDateFormat("dd-MM-yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_osdetail)

        viewManager = LinearLayoutManager(this)
        viewAdapter = AppAdapter(aplicaciones as ArrayList<Aplicacion>, this)

        recyclerView = recyclerView_detalle_os.apply {
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            layoutManager = viewManager
            adapter = viewAdapter
        }

        sistemaOperativo = intent.getParcelableExtra("OS")

        sistemaOperativo?.let {
            HttpRequest.requestHTTP(metodo = "GET", ruta = "/SistemaOperativo/${sistemaOperativo!!.id}/Aplicaciones", callback = { error, datos ->
                if(error){
                    Toast.makeText(this, "Error al obtener aplicaciones", Toast.LENGTH_LONG).show()
                }else{
                    async(UI) {
                            val aplicacionesDef: Deferred<ArrayList<Aplicacion>?> = bg {
                                JsonParser.jsonToAppList(datos)
                            }
                            visualizarDatos(aplicacionesDef.await() as ArrayList<Aplicacion>)
                        }
                }
            }
            )
        }

        btn_nueva_app.setOnClickListener {
            irActividadCreateApp()
        }
    }

    fun llenarVistaOS(sistemaOperativo: SistemaOperativo){
        txtView_nombre_detalle.text = sistemaOperativo.nombre
        txtView_version_detalle.text = sistemaOperativo.version.toString()
        txtView_peso_detalle.text  = sistemaOperativo.pesoEnGigas.toString()
        txtView_instalado_detalle.text = if(sistemaOperativo.instalado) "Sí" else "No"
        txtView_fecha_detalle.text = dateFormat.format(sistemaOperativo.fechaLanzamiento)

    }

    fun irActividadCreateApp(){
        val intent = Intent(this, CreateAppActivity::class.java)
        intent.putExtra("OS_ID", sistemaOperativo!!.id)
        startActivity(intent)
    }


    fun visualizarDatos(aplicacionesRecv: ArrayList<Aplicacion>){
        llenarVistaOS(sistemaOperativo as SistemaOperativo)
        aplicaciones!!.addAll(aplicacionesRecv)
        viewAdapter.notifyDataSetChanged()
    }


}
