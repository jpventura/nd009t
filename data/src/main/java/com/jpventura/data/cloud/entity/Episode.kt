package com.jpventura.data.cloud.entity

import com.google.gson.annotations.SerializedName

data class Episode(
    @SerializedName("airdate") val airDate: String = "",
    @SerializedName("airstamp") val airStamp: String = "",
    @SerializedName("airtime") val airTime: String,
    val id: Long,
    val image: Image? = null,
    @SerializedName("_links") val links: Links,
    val name: String,
    val number: Int,
    val runtime: Int,
    val season: Int,
    val summary: String,
    val url: String
)
