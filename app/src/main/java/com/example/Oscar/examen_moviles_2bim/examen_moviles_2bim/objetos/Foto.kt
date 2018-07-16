package com.example.Oscar.examen_moviles_2bim.objetos

import android.os.Parcel
import android.os.Parcelable

class Foto(var id: Int = -1,
           var datos: String,
           var extension: String,
           var aplicacion: Int = -1) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(datos)
        parcel.writeString(extension)
        parcel.writeInt(aplicacion)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Foto> {
        override fun createFromParcel(parcel: Parcel): Foto {
            return Foto(parcel)
        }

        override fun newArray(size: Int): Array<Foto?> {
            return arrayOfNulls(size)
        }
    }

}