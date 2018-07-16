package com.example.Oscar.examen_moviles_2bim.objetos

import android.os.Parcel
import android.os.Parcelable
import com.beust.klaxon.Json
import com.example.Oscar.examen_moviles_2bim.parser.KlaxonDate
import java.text.SimpleDateFormat
import java.util.*

class SistemaOperativo(var id: Int = -1,
                       var nombre: String,
                       var version: Int,
                       @KlaxonDate var fechaLanzamiento: Date,
                       var pesoEnGigas: Double,
                       var instalado: Boolean,
                       @Json(ignored = true) var aplicaciones: List<Aplicacion>? = null) : Parcelable {

    val dateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            Date(parcel.readLong()),
            parcel.readDouble(),
            parcel.readByte() != 0.toByte(),
            parcel.createTypedArrayList(Aplicacion)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombre)
        parcel.writeInt(version)
        parcel.writeLong(fechaLanzamiento.time)
        parcel.writeDouble(pesoEnGigas)
        parcel.writeByte((if (instalado) 1 else 0).toByte())
        parcel.writeTypedList(aplicaciones)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SistemaOperativo> {
        override fun createFromParcel(parcel: Parcel): SistemaOperativo {
            return SistemaOperativo(parcel)
        }

        override fun newArray(size: Int): Array<SistemaOperativo?> {
            return arrayOfNulls(size)
        }
    }

}