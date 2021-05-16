package com.rohithkadaveru.yelpProto.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Business(
    val name: String,
    val id: String,
    val imageUrl: String?,
    val isClosed: Boolean,
    val url: String,
    val reviewCount: Int,
    val rating: Double,
    val price: String,
    val phone: String?,
    val distance: Double,
    val address: String,
    val latitude: Double,
    val longitude: Double
) : Parcelable