package com.example.myapplication

import android.os.Parcel
import android.os.Parcelable

data class WifiData(val ssid: String, val capabilities: String, val frequency: Int) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(ssid)
        parcel.writeString(capabilities)
        parcel.writeInt(frequency)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WifiData> {
        override fun createFromParcel(parcel: Parcel): WifiData {
            return WifiData(parcel)
        }

        override fun newArray(size: Int): Array<WifiData?> {
            return arrayOfNulls(size)
        }
    }
}