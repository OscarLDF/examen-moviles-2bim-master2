package com.example.Oscar.examen_moviles_2bim.vendedor

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import android.widget.Toast
import com.example.Oscar.examen_moviles_2bim.R
import com.example.Oscar.examen_moviles_2bim.objetos.Aplicacion
import com.example.Oscar.examen_moviles_2bim.objetos.Foto
import com.example.Oscar.examen_moviles_2bim.parser.JsonParser
import com.example.Oscar.examen_moviles_2bim.request.HttpRequest
import com.example.Oscar.examen_moviles_2bim.util.ImageHandler
import kotlinx.android.synthetic.main.activity_create_app.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CreateAppActivity : AppCompatActivity() {

    private val dateFormat = SimpleDateFormat("dd-MM-yyyy")
    private var imagePath = ""
    private lateinit var imageBitmap: Bitmap
    private var aplicacion: Aplicacion? = null
    private var os_id = 0
    companion object {
        val REQUEST_IMAGE_CAPTURE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_app)

        aplicacion = intent.getParcelableExtra("APP")
        os_id = intent.getIntExtra("OS_ID", 0)

        aplicacion?.let {
            llenarFormulario(aplicacion as Aplicacion)
        }

        btn_tomar_foto.setOnClickListener {
            tomarFoto()
        }

        btn_guardar_app.setOnClickListener {
            if(aplicacion == null){
                val nuevaAplicacion = crearAplicacion()
                val appJson = JsonParser.appToJson(nuevaAplicacion)

                HttpRequest.requestHTTP("POST", "/Aplicacion", appJson, { error, datos ->
                    if(error){
                        Toast.makeText(this, "Error al crear aplicacion", Toast.LENGTH_LONG).show()
                    }else{
                        val aplicacionCreada = JsonParser.jsonToApp(datos)
                        nuevaAplicacion.foto!!.aplicacion = aplicacionCreada!!.id
                        val fotoJson = JsonParser.fotoToJson(nuevaAplicacion.foto as Foto)
                        HttpRequest.requestHTTP("POST", "/Foto", fotoJson, { error, datos ->
                            if(error){
                                Toast.makeText(this, "Error al crear foto", Toast.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(this, "Aplicación creada", Toast.LENGTH_LONG).show()
                            }
                        })
                    }
                })
            }else{
                actualizarApp(aplicacion!!)
                val appJson = JsonParser.appToJson(aplicacion as Aplicacion)
                HttpRequest.requestHTTP("PUT", "/Aplicacion/${aplicacion!!.id}", appJson, { error, datos ->
                    if(error){
                        Toast.makeText(this, "Error al actualizar", Toast.LENGTH_LONG).show()
                    }else{
                        val fotoJson = JsonParser.fotoToJson(aplicacion!!.foto as Foto)
                        HttpRequest.requestHTTP("PUT", "/Foto/${aplicacion!!.foto!!.id}", fotoJson, { error, datos ->
                            if(error){
                                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(this, "Aplicación actualizada", Toast.LENGTH_LONG).show()
                            }
                        })
                    }
                })
            }
        }
    }

    fun actualizarApp(aplicacion: Aplicacion){
        val nombre = editText_nombre_app.text.toString()
        val version = editText_version_app.text.toString().toInt()
        val costo = editText_costo_app.text.toString().toDouble()
        val pesoEnGigas = editText_peso_app.text.toString().toDouble()
        val fechaLanzamiento = dateFormat.parse(editText_fecha_app.text.toString())
        val urlDescarga = editText_url_app.text.toString()
        val fotoEncoded = ImageHandler.bitmapToB64String(imageBitmap)
        aplicacion.nombre = nombre
        aplicacion.version = version
        aplicacion.costo = costo
        aplicacion.pesoEnGigas = pesoEnGigas
        aplicacion.fechaLanzamiento = fechaLanzamiento
        aplicacion.urlDescarga = urlDescarga
        aplicacion.foto!!.datos = fotoEncoded
    }

    fun crearAplicacion(): Aplicacion{
        val nombre = editText_nombre_app.text.toString()
        val version = editText_version_app.text.toString().toInt()
        val costo = editText_costo_app.text.toString().toDouble()
        val pesoEnGigas = editText_peso_app.text.toString().toDouble()
        val fechaLanzamiento = dateFormat.parse(editText_fecha_app.text.toString())
        val urlDescarga = editText_url_app.text.toString()
        val fotoEncoded = ImageHandler.bitmapToB64String(imageBitmap)
        val foto = Foto(datos = fotoEncoded, extension = "jpg")

        val aplicacion = Aplicacion(
                nombre = nombre,
                version = version,
                costo = costo,
                pesoEnGigas = pesoEnGigas,
                fechaLanzamiento = fechaLanzamiento,
                urlDescarga = urlDescarga,
                foto = foto,
                sistemaOperativo = os_id
        )
        return aplicacion
    }

    fun llenarFormulario(aplicacion: Aplicacion){
        editText_nombre_app.append(aplicacion.nombre)
        editText_peso_app.append(aplicacion.pesoEnGigas.toString())
        editText_costo_app.append(aplicacion.costo.toString())
        editText_url_app.append(aplicacion.urlDescarga)
        editText_version_app.append(aplicacion.version.toString())
        editText_fecha_app.append(dateFormat.format(aplicacion.fechaLanzamiento))
        imageBitmap = ImageHandler.base64ToBitmap(aplicacion.foto!!.datos)
        imgView_foto_app.setImageBitmap(imageBitmap)
    }

    fun tomarFotoIntent(file: File){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imagePath = file.absolutePath
        val photoUri: Uri = FileProvider.getUriForFile(
                this,
                "com.example.daniel.examen_moviles_2bim.fileprovider",
                file
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        if(intent.resolveActivity(packageManager) != null){
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            Log.i("IMAGE_PATH", imagePath)
            if(aplicacion != null)
                imageBitmap.recycle()
            afterImageRotationCompleted(ImageHandler.bitmapFromFileRotation(File(imagePath)))
            /*async(UI){
                val imageRotated: Deferred<Bitmap> = bg{
                    ImageHandler.bitmapFromFileRotation(File(imagePath))
                }
                afterImageRotationCompleted(imageRotated.await())
            }*/
        }
    }


    fun afterImageRotationCompleted(bitmap: Bitmap){
        imageBitmap = bitmap
        imgView_foto_app.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap, imgView_foto_app.width, imgView_foto_app.height, false))
    }

    fun tomarFoto(){
        val imageFile = createImageFile("JPEG_", Environment.DIRECTORY_PICTURES, ".jpg")
        tomarFotoIntent(imageFile)
    }

    fun createImageFile(prefix: String, directory: String, extension: String): File {
        val timestamp = SimpleDateFormat("ddMMyyyy_HHmmss").format(Date())
        val filename = prefix + timestamp + "_"
        val storageDir = getExternalFilesDir(directory)
        return File.createTempFile(filename, extension, storageDir)
    }
}
