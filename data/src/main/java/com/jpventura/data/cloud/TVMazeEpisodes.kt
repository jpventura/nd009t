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
import com.jpventura.domain.bean.Episode
import com.jpventura.domain.model.TelevisionSeriesModel
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class TVMazeEpisodes : TelevisionSeriesModel.Episodes {

    override fun clear(): Single<Int> = TODO()

    override fun containsKey(key: String): Single<Boolean> = TODO()

    override fun containsKeys(keys: Collection<String>): Single<Boolean> = TODO()

    override fun containsValue(value: Episode): Completable = TODO()

    override fun containsValues(values: Collection<Episode>): Completable = TODO()

    override fun create(values: Collection<Episode>): Single<List<String>> = TODO()

    override fun createOne(value: Episode): Single<String> = TODO()

    override fun createOrUpdate(values: Collection<Episode>): Single<List<String>> = TODO()

    override fun createOrUpdateOne(value: Episode): Single<String> = TODO()

    override fun destroy(keys: Collection<String>): Single<List<String>> = TODO()

    override fun destroyOne(key: String): Single<String> = TODO()

    override fun find(keys: Collection<String>): Observable<List<Episode>> = TODO()

    override fun find(query: Map<String, Any>): Observable<List<Episode>> = TODO()

    override fun <U : Bean<String>> find(parent: U): Observable<List<Episode>> = TODO()

    override fun findOne(key: String): Single<Episode> = TODO()

    override fun keys(): Observable<List<String>> = TODO()

    override fun size(): Observable<Int> = TODO()

    override fun update(values: Collection<Episode>): Single<Int> = TODO()

    override fun updateOne(value: Episode): Single<Int> = TODO()

}
