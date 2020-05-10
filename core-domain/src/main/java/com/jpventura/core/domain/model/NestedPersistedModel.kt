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

interface NestedPersistedModel<P, K, V : NestedBean<P, K>> : NestedModel<P, K, V>, PersistedModel<String, V> {

    override fun clear(): Single<Int>

    override fun containsKey(key: String): Single<Boolean>

    override fun containsKeys(keys: Collection<String>): Single<Boolean>

    override fun containsValue(value: V): Completable

    override fun containsValues(values: Collection<V>): Completable

    override fun create(values: Collection<V>): Single<List<String>>

    override fun createOne(value: V): Single<String>

    override fun createOrUpdate(values: Collection<V>): Single<List<String>>

    override fun createOrUpdateOne(value: V): Single<String>

    override fun destroy(keys: Collection<String>): Single<List<String>>

    override fun destroyOne(key: String): Single<String>

    override fun find(keys: Collection<String>): Observable<List<V>>

    override fun find(query: Map<String, Any?>): Observable<List<V>>

    override fun <U : Bean<P>> find(parent: U): Observable<List<V>>

    override fun findOne(key: String): Single<V>

    override fun keys(): Observable<List<String>>

    override fun size(): Observable<Int>

    override fun update(values: Collection<V>): Single<Int>

    override fun updateOne(value: V): Single<Int>

}
