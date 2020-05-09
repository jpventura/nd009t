package com.jpventura.data.cloud.entity

import com.google.gson.annotations.SerializedName

data class Links(
    @SerializedName("previousepisode") val previousEpisode: PreviousEpisode
)
