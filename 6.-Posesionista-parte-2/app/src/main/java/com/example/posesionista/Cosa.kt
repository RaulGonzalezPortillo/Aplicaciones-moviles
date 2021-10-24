package com.example.posesionista

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class Cosa(): Parcelable {
    var nombre: String = "Nueva cosa"
    var precio: Int = 0
    //Tomamos los primeros 6 caracteres de un UUID aleatorio
    var numeroSerie: String = UUID.randomUUID().toString().substring(0, 6)
    //var numeroSerie = ""
    var fecha: Date = Date()
    var id = UUID.randomUUID().toString().substring(0, 6)

    constructor(parcel: Parcel): this() {
        nombre = parcel.readString().toString()
        precio = parcel.readInt()
        numeroSerie = parcel.readString().toString()
        fecha = parcel.readSerializable() as Date
        id = parcel.readString().toString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(nombre)
        dest.writeInt(precio)
        dest.writeString(numeroSerie)
        dest.writeSerializable(fecha)
    }

    companion object CREATOR: Parcelable.Creator<Cosa> {
        override fun createFromParcel(source: Parcel): Cosa {
            return Cosa(source)
        }

        override fun newArray(size: Int): Array<Cosa?> {
            return arrayOfNulls(size)
        }
    }
}