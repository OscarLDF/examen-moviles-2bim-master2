package com.example.Oscar.examen_moviles_2bim.adaptadores

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.Oscar.examen_moviles_2bim.R
import com.example.Oscar.examen_moviles_2bim.objetos.SistemaOperativo
import com.example.Oscar.examen_moviles_2bim.request.HttpRequest
import com.example.Oscar.examen_moviles_2bim.vendedor.CreateOSActivity
import com.example.Oscar.examen_moviles_2bim.vendedor.OSDetailActivity

class OSAdapter(private val myDataset: ArrayList<SistemaOperativo>, var context: Context): RecyclerView.Adapter<OSAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnCreateContextMenuListener{

        var textView_nombre: TextView
        var textView_version: TextView
        var textView_peso: TextView
        var btn_detalle_fila: Button

        lateinit var sistema: SistemaOperativo
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        init {
            textView_nombre = view.findViewById(R.id.txtView_nombre_fila) as TextView
            textView_version = view.findViewById(R.id.txtView_version_fila) as TextView
            textView_peso = view.findViewById(R.id.txtView_peso_fila) as TextView
            btn_detalle_fila = view.findViewById(R.id.btn_detalle_fila) as Button

            btn_detalle_fila.setOnClickListener {
                irActividadDetalleOS()
            }

            view.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            val menuInflater = MenuInflater(context)
            menuInflater.inflate(R.menu.context_menu, menu)

            menu?.findItem(R.id.editar)?.setOnMenuItemClickListener {
                irActividadCreacionOS()
                true
            }

            menu?.findItem(R.id.eliminar)?.setOnMenuItemClickListener {
                crearDialogo()
                true
            }
        }


        fun crearDialogo(){
            builder.setMessage("¿Eliminar datos?")
                    .setPositiveButton("Sí",  { dialog, which ->
                        eliminarOS(sistema)
                        true
                    })
                    .setNegativeButton("No", { dialog, which ->
                        true
                    })
            val dialogo = builder.create()
            dialogo.show()
        }

        fun irActividadCreacionOS(){
            val intent = Intent(context, CreateOSActivity::class.java)
            intent.putExtra("OS", sistema)
            context.startActivity(intent)
        }

        fun eliminarOS(sistemaOperativo: SistemaOperativo){
            HttpRequest.requestHTTP("DELETE", "/SistemaOperativo/${sistemaOperativo.id}", callback = { error, datos ->
                if(error){
                    Toast.makeText(context, "Error al eliminar el sistema", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(context, "Sistema eliminado", Toast.LENGTH_LONG).show()
                    myDataset.remove(sistemaOperativo)
                    notifyDataSetChanged()
                }
            })
        }

        fun irActividadDetalleOS(){
            val intent = Intent(context, OSDetailActivity::class.java)
            intent.putExtra("OS", sistema)
            context.startActivity(intent)
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sistemaActual: SistemaOperativo = myDataset[position]
        holder.textView_nombre.text = sistemaActual.nombre
        holder.textView_peso.text = sistemaActual.pesoEnGigas.toString()
        holder.textView_version.text = sistemaActual.version.toString()
        holder.sistema = sistemaActual

    }

    override fun getItemCount(): Int {
        return myDataset.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_row, parent, false)
        return ViewHolder(itemView)
    }
}

