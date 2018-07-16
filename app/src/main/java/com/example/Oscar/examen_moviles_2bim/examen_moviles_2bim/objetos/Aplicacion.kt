package com.example.Oscar.examen_moviles_2bim.objetos

import android.os.Parcel
import android.os.Parcelable
import com.beust.klaxon.Json
import com.example.Oscar.examen_moviles_2bim.parser.KlaxonDate
import java.util.*

class Aplicacion(var id: Int = -1,
                 var nombre: String,
                 var pesoEnGigas: Double,
                 var version: Int,
                 var urlDescarga: String,
                 @KlaxonDate var fechaLanzamiento: Date,
                 var costo: Double,
                 var sistemaOperativo: Int = -1,
                 var detalleOrden: Int = -1,
                 var foto: Foto? = null): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readDouble(),
            parcel.readInt(),
            parcel.readString(),
            Date(parcel.readLong()),
            parcel.readDouble(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readParcelable(Foto::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombre)
        parcel.writeDouble(pesoEnGigas)
        parcel.writeInt(version)
        parcel.writeString(urlDescarga)
        parcel.writeLong(fechaLanzamiento.time)
        parcel.writeDouble(costo)
        parcel.writeInt(sistemaOperativo)
        parcel.writeInt(detalleOrden)
        parcel.writeParcelable(foto, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Aplicacion> {
        override fun createFromParcel(parcel: Parcel): Aplicacion {
            return Aplicacion(parcel)
        }

        override fun newArray(size: Int): Array<Aplicacion?> {
            return arrayOfNulls(size)
        }
    }


}
