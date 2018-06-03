package br.com.lravanelli.findpets.model

import android.os.Parcel
import android.os.Parcelable

data class Pet (val id: Int,
                val nome: String,
                val especie: String,
                val raca: String,
                val tel: String,
                val cep: String,
                val obs: String,
                val id_user: Int,
                val path_foto: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nome)
        parcel.writeString(especie)
        parcel.writeString(raca)
        parcel.writeString(tel)
        parcel.writeString(cep)
        parcel.writeString(obs)
        parcel.writeInt(id_user)
        parcel.writeString(path_foto)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Pet> {
        override fun createFromParcel(parcel: Parcel): Pet {
            return Pet(parcel)
        }

        override fun newArray(size: Int): Array<Pet?> {
            return arrayOfNulls(size)
        }
    }
}
