package com.jpventura.data.cache

import com.jpventura.core.domain.model.MemoryNestedModel
import com.jpventura.domain.bean.Episode
import com.jpventura.domain.model.TelevisionSeriesModel
import io.reactivex.Single

class CacheEpisodeModel : TelevisionSeriesModel.Episodes, MemoryNestedModel<Long, Long, Episode>() {

    override fun findOne(parentId: Long, id: Long): Single<Episode> {
        return super.findOne("$parentId/$id")
    }

}
