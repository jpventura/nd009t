package com.jpventura.data.cloud.entity


import com.google.gson.annotations.SerializedName

data class Season(
    val endDate: String = "",
    val episodeOrder: Int = 0,
    val id: Int = 0,
    val image: Any? = null,
    @SerializedName("_links")
    val links: LinksXX = Links(),
    val name: String = "",
    val network: NetworkX = Network(),
    val number: Int = 0,
    val premiereDate: String = "",
    val summary: Any? = null,
    val url: String = "",
    val webChannel: Any? = null
)