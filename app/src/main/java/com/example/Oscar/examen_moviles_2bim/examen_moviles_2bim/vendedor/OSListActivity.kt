package com.example.Oscar.examen_moviles_2bim.vendedor

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.example.Oscar.examen_moviles_2bim.R
import com.example.Oscar.examen_moviles_2bim.adaptadores.OSAdapter
import com.example.Oscar.examen_moviles_2bim.objetos.SistemaOperativo
import com.example.Oscar.examen_moviles_2bim.parser.JsonParser
import com.example.Oscar.examen_moviles_2bim.request.HttpRequest
import kotlinx.android.synthetic.main.activity_oslist.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import kotlin.collections.ArrayList

class OSListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    var sistemasOperativos = ArrayList<SistemaOperativo>()
    private lateinit var sistemasOperativosDef: Deferred<ArrayList<SistemaOperativo>?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oslist)


        viewManager = LinearLayoutManager(this)
        viewAdapter = OSAdapter(sistemasOperativos, this)

        recyclerView = os_recycler.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            itemAnimator = DefaultItemAnimator()
            adapter = viewAdapter
        }

        HttpRequest.requestHTTP("GET", "/SistemaOperativo", callback = { error, datos ->
            if(error){
                Toast.makeText(this, "Error al obtener los datos", Toast.LENGTH_LONG).show()
            }else{
                async(UI){
                    sistemasOperativosDef = bg {
                        JsonParser.jsonToOSList(datos)
                    }
                    visualizarDatos(sistemasOperativosDef.await() as ArrayList<SistemaOperativo>)
                }
            }
        })
    }

    fun visualizarDatos(sistemasOperativosRecv: ArrayList<SistemaOperativo>){
        sistemasOperativos.addAll(sistemasOperativosRecv)
        viewAdapter.notifyDataSetChanged()
    }

}
