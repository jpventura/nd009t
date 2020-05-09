package com.jpventura.data.cloud.entity

import com.google.gson.annotations.SerializedName

data class WebChannel(
    val country: Country,
    val id: Int = 0,
    val name: String = ""
)
