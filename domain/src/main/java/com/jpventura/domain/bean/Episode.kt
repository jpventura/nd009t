package com.jpventura.domain.bean

import com.jpventura.core.domain.bean.NestedBean

data class Episode(
    override val key: Long,
    override val parentKey: Long
) : NestedBean<Long, Long>
