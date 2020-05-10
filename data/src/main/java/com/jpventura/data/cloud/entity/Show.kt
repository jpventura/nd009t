package com.jpventura.data.cloud.entity

import com.google.gson.annotations.SerializedName

data class Show(
    val genres: List<String> = emptyList(),
    val id: Long? = 0L,
    val image: Image? = null,
    val language: String = "",
    @SerializedName("_links") val links: Links,
    val name: String = "",
    val network: Network,
    val officialSite: String = "",
    val premiered: String = "",
    val rating: Rating,
    val runtime: Int = 0,
    val schedule: Schedule = Schedule(),
    val status: String = "",
    val summary: String = "",
    val type: String = "",
    val updated: Int,
    val url: String = "",
    val webChannel: WebChannel,
    val weight: Int
)
