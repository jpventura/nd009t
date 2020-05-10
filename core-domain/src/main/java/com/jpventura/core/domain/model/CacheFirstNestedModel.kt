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

package com.jpventura.core.domain.model

import com.jpventura.core.domain.bean.Bean
import com.jpventura.core.domain.bean.NestedBean
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction

abstract class CacheFirstNestedModel<P, K, V : NestedBean<P, K>>(
    private val cache: NestedPersistedModel<P, K, V>,
    private val cloud: NestedModel<P, K, V>
) : NestedPersistedModel<P, K, V> {

    override fun clear() = cache.clear()

    override fun create(values: Collection<V>) = cache.create(values)

    override fun createOne(value: V) = cache.createOne(value)

    override fun createOrUpdate(values: Collection<V>) = cache.createOrUpdate(values)

    override fun createOrUpdateOne(value: V) = cache.createOrUpdateOne(value)

    override fun destroy(keys: Collection<String>) = cache.destroy(keys)

    override fun destroyOne(key: String): Single<String> = cache.destroyOne(key)

    override fun update(values: Collection<V>): Single<Int> = cache.update(values)

    override fun updateOne(value: V): Single<Int> = cache.updateOne(value)

    override fun containsKey(key: String): Single<Boolean> = containsKey(key)

    override fun containsKeys(keys: Collection<String>): Single<Boolean> = containsKeys(keys)

    override fun containsValue(value: V): Completable = containsValue(value)

    override fun containsValues(values: Collection<V>): Completable = containsValues(values)

    override fun <U : Bean<P>> find(parent: U): Observable<List<V>> =
        Observable.combineLatest(
            cache.find(parent),
            cloud.find(parent),
            BiFunction { cache, cloud -> cache.putAll(cloud) }
        )

    override fun find(keys: Collection<String>): Observable<List<V>> =
        Observable.combineLatest(
            cache.find(keys),
            cloud.find(keys),
            BiFunction { cache, cloud -> cache.putAll(cloud) }
        )

    override fun find(query: Map<String, Any?>): Observable<List<V>> =
        Observable.combineLatest(
            cache.find(query),
            cloud.find(query),
            BiFunction { cache, cloud -> cache.putAll(cloud) }
        )

    override fun findOne(key: String): Single<V> =
        cloud.findOne(key)
            .onErrorResumeNext(cache.findOne(key))
            .flatMap {
                cache.createOrUpdateOne(it)
            }
            .flatMap {
                cache.findOne(key)
            }

    override fun keys() = cache.keys()

    override fun size() = cache.size()

}

private fun <K, V : Bean<K>>List<V>.putAll(values: Collection<V>): List<V> {
    val cache = map { it.key to it }.toMap().toMutableMap()
    val cloud = values.map { it.key to it }.toMap()
    cache.putAll(cloud)
    return cloud.values.toList()
}
