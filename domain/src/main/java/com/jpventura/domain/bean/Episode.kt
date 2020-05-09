package com.jpventura.domain.bean

import com.jpventura.core.domain.bean.NestedBean

data class Episode(
    override val key: String,
    override val parentKey: String
) : NestedBean<String, String>
