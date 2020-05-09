package com.jpventura.data.cloud.entity

import com.google.gson.annotations.SerializedName

data class Show(
//    val externals: Externals = Externals,
//    val genres: List<String> = listOf(),
//    val id: Int,
//    val name: String,
//    val image: Image,
//    val schedule: Schedule,
//    val summary: String,
//    val updated: Long,

    val language: String = "",
    @SerializedName("_links") val links: Links,
    val name: String,
    val network: Network,
    val officialSite: String,
    val premiered: String,
    val rating: Rating,
    val runtime: Int,
    val schedule: Schedule = Schedule(),
    val status: String,
    val type: String,
    val updated: Int,
    val url: String,
    val webChannel: WebChannel,
    val weight: Int
)
