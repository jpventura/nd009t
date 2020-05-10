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

package com.jpventura.data.cloud

import com.jpventura.core.domain.bean.Bean
import com.jpventura.data.ktx.asBean
import com.jpventura.domain.bean.Episode
import com.jpventura.domain.model.TelevisionSeriesModel
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class TVMazeEpisodes(
    private val client: TVMazeClient
) : TelevisionSeriesModel.Episodes {

    override fun clear(): Single<Int> =
        Single.error(UnsupportedOperationException())

    override fun containsKey(key: String): Single<Boolean> =
        Single.error(UnsupportedOperationException())

    override fun containsKeys(keys: Collection<String>): Single<Boolean> =
        Single.error(UnsupportedOperationException())

    override fun containsValue(value: Episode): Completable =
        Completable.error(UnsupportedOperationException())

    override fun containsValues(values: Collection<Episode>): Completable =
        Completable.error(UnsupportedOperationException())

    override fun create(values: Collection<Episode>): Single<List<String>> =
        Single.error(UnsupportedOperationException())

    override fun createOne(value: Episode): Single<String> =
        Single.error(UnsupportedOperationException())

    override fun createOrUpdate(values: Collection<Episode>): Single<List<String>> =
        Single.error(UnsupportedOperationException())

    override fun createOrUpdateOne(value: Episode): Single<String> =
        Single.error(UnsupportedOperationException())

    override fun destroy(keys: Collection<String>): Single<List<String>> =
        Single.error(UnsupportedOperationException())

    override fun destroyOne(key: String): Single<String> =
        Single.error(UnsupportedOperationException())

    override fun find(keys: Collection<String>): Observable<List<Episode>> =
        Observable.error(UnsupportedOperationException())

    override fun find(query: Map<String, Any?>): Observable<List<Episode>> {
        val page = query["page"] as? Int ?: 1
        val parentId = query["showId"] as? Long ?: 0L

        return client.getEpisodes(showId = parentId, page = page)
            .toObservable()
            .flatMapIterable { it }
            .map { it.asBean(parentId) }
            .toList()
            .toObservable()
    }

    override fun <U : Bean<Long>> find(parent: U): Observable<List<Episode>> {
        return client.getEpisodes(showId = parent.key)
            .toObservable()
            .flatMapIterable { it }
            .map { it.asBean(parent.key) }
            .toList()
            .toObservable()
    }

    override fun findOne(key: String): Single<Episode> {
        val keys = key.split("/").take(2).map { it.toLong() }
        val parentId = keys[0]
        val id = keys[1]

        return client.getEpisode(id = id).map {
            it.asBean(parentId)
        }
    }

    override fun findOne(parentId: Long, id: Long): Single<Episode> =
        client.getEpisode(id).map { it.asBean(parentId) }

    override fun keys(): Observable<List<String>> =
        Observable.error(UnsupportedOperationException())

    override fun size(): Observable<Int> =
        Observable.error(UnsupportedOperationException())

    override fun update(values: Collection<Episode>): Single<Int> =
        Single.error(UnsupportedOperationException())

    override fun updateOne(value: Episode): Single<Int> =
        Single.error(UnsupportedOperationException())

}
