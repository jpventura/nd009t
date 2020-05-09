package com.jpventura.data.cloud.entity


import com.google.gson.annotations.SerializedName

data class Schedule(
    val days: List<String> = listOf(),
    val time: String = ""
)