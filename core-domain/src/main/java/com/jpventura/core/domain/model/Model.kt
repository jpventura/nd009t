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

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface Model<K, V> {

    fun containsKey(key: K): Single<Boolean>

    fun containsKeys(keys: Collection<K>): Single<Boolean>

    fun containsValue(value: V): Completable

    fun containsValues(values: Collection<V>): Completable

    fun find(keys: Collection<K>): Observable<List<V>>

    fun find(query: Map<String, Any?> = emptyMap()): Observable<List<V>>

    fun findOne(key: K): Single<V>

    fun keys(): Observable<List<K>>

    fun size(): Observable<Int>

}
