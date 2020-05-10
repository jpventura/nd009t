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

import com.jpventura.data.ktx.asBean
import com.jpventura.domain.bean.Show
import com.jpventura.domain.model.TelevisionSeriesModel
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class TVMazeSeries(private val client: TVMazeClient) : TelevisionSeriesModel.Series {

    override fun clear(): Single<Int> = Single.error(UnsupportedOperationException())

    override fun containsKey(key: Long): Single<Boolean> =
        Single.error(UnsupportedOperationException())

    override fun containsKeys(keys: Collection<Long>): Single<Boolean> =
        Single.error(UnsupportedOperationException())

    override fun containsValue(value: Show): Completable =
        Completable.error(UnsupportedOperationException())

    override fun containsValues(values: Collection<Show>): Completable =
        Completable.error(UnsupportedOperationException())

    override fun create(values: Collection<Show>): Single<List<Long>> =
        Single.error(UnsupportedOperationException())

    override fun createOne(value: Show): Single<Long> =
        Single.error(UnsupportedOperationException())

    override fun createOrUpdate(values: Collection<Show>): Single<List<Long>> =
        Single.error(UnsupportedOperationException())

    override fun createOrUpdateOne(value: Show): Single<Long> =
        Single.error(UnsupportedOperationException())

    override fun destroy(keys: Collection<Long>): Single<List<Long>> =
        Single.error(UnsupportedOperationException())

    override fun destroyOne(key: Long): Single<Long> =
        Single.error(UnsupportedOperationException())

    override fun find(keys: Collection<Long>): Observable<List<Show>> = TODO()

    override fun find(query: Map<String, Any?>): Observable<List<Show>> {
        val name = query["name"] as String?
        val page: Int = query["page"] as? Int ?: 1
        val q = query["query"] as String?

        val shows = if (name.isNullOrBlank()) {
            client.getShows(page = page, query = q)
        } else {
            client.searchShows(name = name, page = page)
                .toObservable()
                .flatMapIterable { it }
                .sorted { a, b -> -1 * a.score.compareTo(b.score) }
                .map { it.show }
                .toList()
        }

        return shows.toObservable()
            .flatMapIterable { it }
            .sorted { a, b ->
                val pos = -1 * a.rating.average.compareTo(b.rating.average)
                if (pos == 0) -1 * a.updated.compareTo(b.updated) else pos
            }
            .map { it.asBean() }
            .toList().toObservable()

    }

    override fun findOne(key: Long): Single<Show> = client.getShow(id = key).map { it.asBean() }

    override fun keys(): Observable<List<Long>> = Observable.error(UnsupportedOperationException())

    override fun size(): Observable<Int> = Observable.error(UnsupportedOperationException())

    override fun update(values: Collection<Show>): Single<Int> =
        Single.error(UnsupportedOperationException())

    override fun toggleOne(id: Long, isChecked: Boolean): Single<Show> =
        Single.error(UnsupportedOperationException())

    override fun updateOne(value: Show): Single<Int> =
        Single.error(UnsupportedOperationException())

}
