package com.example.eva_shop

import android.os.Parcel
import android.os.Parcelable

data class Product(
    val name: String,
    val price: String,
    val image: Int,
    var isFavorite: Boolean,
    var isInCart: Boolean = false,
    var description: String // Tambahkan deskripsi produk
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(price)
        parcel.writeInt(image)
        parcel.writeByte(if (isFavorite) 1 else 0)
        parcel.writeByte(if (isInCart) 1 else 0)
        parcel.writeString(description)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}
