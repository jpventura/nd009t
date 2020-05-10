/**
 * Copyright (c) 2020 Joao Paulo Fernandes Ventura
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package com.jpventura.data.ktx

import com.jpventura.domain.bean.Episode
import com.jpventura.domain.bean.episode
import com.jpventura.domain.bean.schedule
import com.jpventura.domain.bean.Show
import com.jpventura.domain.bean.show
import com.jpventura.data.cloud.entity.Episode as EpisodeEntity
import com.jpventura.data.cloud.entity.Show as Series

fun EpisodeEntity.asBean(parentId: Long): Episode = episode {
    id = this@asBean.id
    name = this@asBean.name
    number = this@asBean.number
    image = this@asBean.image?.let { if (it.original.isBlank()) it.medium else it.original } ?: ""
    season = this@asBean.season
    seriesId = parentId
    summary = this@asBean.summary
}

fun Series.asBean(): Show = show {
    id = this@asBean.id
    genres = this@asBean.genres
    name = this@asBean.name
    poster = this@asBean.image?.original ?: this@asBean.image?.medium ?: ""
    rating = this@asBean.rating.average
    schedule = schedule {
        days = this@asBean.schedule.days
        time = this@asBean.schedule.time
    }
    summary = this@asBean.summary
}
