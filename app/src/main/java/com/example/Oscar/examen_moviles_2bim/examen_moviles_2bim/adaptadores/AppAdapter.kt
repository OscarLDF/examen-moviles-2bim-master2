package com.example.Oscar.examen_moviles_2bim.adaptadores

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.Oscar.examen_moviles_2bim.R
import com.example.Oscar.examen_moviles_2bim.objetos.Aplicacion
import com.example.Oscar.examen_moviles_2bim.request.HttpRequest
import com.example.Oscar.examen_moviles_2bim.util.ImageHandler
import com.example.Oscar.examen_moviles_2bim.vendedor.CreateAppActivity

class AppAdapter(private val myDataset: ArrayList<Aplicacion>, var context: Context): RecyclerView.Adapter<AppAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnCreateContextMenuListener {
        var txtView_nombre: TextView
        var txtView_version: TextView
        var imgView_foto: ImageView
        var btn_detalle_app: Button

        lateinit var aplicacion: Aplicacion
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)

        init {
            txtView_nombre = view.findViewById(R.id.txtView_nombre_fila) as TextView
            txtView_version = view.findViewById(R.id.txtView_version_fila) as TextView
            imgView_foto = view.findViewById(R.id.imgView_foto_app_row) as ImageView

            btn_detalle_app = view.findViewById(R.id.btn_detalle_fila) as Button

            btn_detalle_app.setOnClickListener {
                irActividadCreateApp()
            }
            view.setOnCreateContextMenuListener(this)

        }

        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            val menuInflater: MenuInflater = MenuInflater(context)
            menuInflater.inflate(R.menu.context_menu, menu)

            menu?.findItem(R.id.editar)?.setOnMenuItemClickListener {
                irActividadCreateApp()
                true
            }

            menu?.findItem(R.id.eliminar)?.setOnMenuItemClickListener {
                crearDialogo()
                true
            }
        }


        fun crearDialogo(){
            builder.setMessage("¿Eliminar datos?")
                    .setPositiveButton("Sí", { dialog, which ->
                        eliminarAplicacion(aplicacion)
                        true
                    })
                    .setNegativeButton("No", { dialog, which ->
                        true
                    })
            val dialogo = builder.create()
            dialogo.show()
        }

        fun irActividadCreateApp() {
            val intent = Intent(context, CreateAppActivity::class.java)
            intent.putExtra("APP", aplicacion)
            context.startActivity(intent)
        }


        fun eliminarAplicacion(aplicacion: Aplicacion){
            HttpRequest.requestHTTP("DELETE", "/Aplicacion/${aplicacion.id}", callback = { error, datos ->
                if(error){
                    Toast.makeText(context, "Error al eliminar la app", Toast.LENGTH_LONG)
                }else{
                    HttpRequest.requestHTTP("DELETE", "/Foto/${aplicacion.foto!!.id}", callback = { error, datos ->
                        if(error){
                            Toast.makeText(context, "Error al eliminar la app", Toast.LENGTH_LONG)
                        }else{
                            Toast.makeText(context, "Aplicación eliminada", Toast.LENGTH_LONG)
                            myDataset.remove(aplicacion)
                            notifyDataSetChanged()
                        }
                    })
                }
            })
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val aplicacionActual = myDataset[position]
        holder.txtView_nombre.text = aplicacionActual.nombre
        holder.txtView_version.text = aplicacionActual.version.toString()
        holder.imgView_foto.setImageBitmap(ImageHandler.base64ToBitmap(aplicacionActual.foto!!.datos))
        holder.aplicacion = aplicacionActual
    }

    override fun getItemCount(): Int {
        return myDataset.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_app_row, parent, false)

        return ViewHolder(itemView)
    }

}