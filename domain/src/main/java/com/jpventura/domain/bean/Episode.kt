package com.jpventura.domain.bean

import com.jpventura.core.domain.bean.NestedBean

data class Episode(
    override val key: Long,
    override val parentKey: Long,
    val name: String,
    val number: Int,
    val season: Int,
    val summary: String,
    val image: String
) : NestedBean<Long, Long>