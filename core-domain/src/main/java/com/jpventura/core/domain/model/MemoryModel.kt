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
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import kotlin.math.max

abstract class MemoryModel<K, V : Bean<K>>(
    model: Map<K, V> = emptyMap()
) : java.util.Observable(), PersistedModel<K, V> {

    private val model: MutableMap<K, V> = model.toMutableMap()

    private val subject = BehaviorSubject.create<List<V>>()

    init {
        super.setChanged()
        subject.onNext(model.values.toList())
    }

    override fun clear(): Single<Int> {
        val size = model.size

        if (model.isNotEmpty()) {
            model.clear()
            setChanged()
            notifyObservers()
        }

        return Single.just(size)
    }

    override fun containsKey(key: K) = Single.just(key in model)

    override fun containsKeys(keys: Collection<K>): Single<Boolean> {
        val all = keys.subtract(model.keys).isEmpty()
        return Single.just(all)
    }

    override fun containsValue(value: V) = containsValues(setOf(value))

    override fun containsValues(values: Collection<V>) = Completable.create { emitter ->
        val difference = values.toSet().subtract(model.values)

        if (difference.isEmpty()) {
            emitter.onComplete()
        } else {
            emitter.onError(NoSuchElementException("No such element: ${difference.first()}"))
        }
    }

    override fun create(values: Collection<V>): Single<List<K>> {
        val entries = values.map { it.key to it }
        val collision = entries.intersect(model.entries)

        if (collision.isNotEmpty()) {
            val value = collision.first()
            return Single.error(IllegalStateException("Value already contained: $value"))
        }

        model.putAll(entries)
        setChanged()
        notifyObservers()

        return Single.just(values.map { it.key })
    }

    override fun createOne(value: V): Single<K> = create(listOf(value)).map { it.first() }

    override fun createOrUpdate(values: Collection<V>): Single<List<K>> {
        val updates = values.filter { !model.values.contains(it) }.map { it.key to it }.toMap()
        val keys = values.map { it.key }.toList()

        return if (updates.isEmpty()) {
            Single.just(emptyList())
        } else {
            model.putAll(updates)
            setChanged()
            notifyObservers()
            Single.just(keys)
        }
    }

    override fun createOrUpdateOne(value: V) = createOrUpdate(listOf(value)).map { it.first() }

    override fun destroy(keys: Collection<K>): Single<List<K>> {
        val destroyed = model.filterKeys { keys.contains(it) }.map {
            model.remove(it.key)
            it.key
        }

        if (destroyed.isNotEmpty()) {
            setChanged()
            notifyObservers()
        }

        return Single.just(destroyed)
    }

    override fun destroyOne(key: K): Single<K> = destroy(listOf(key)).map { it.first() }

    override fun find(keys: Collection<K>): Observable<List<V>> = subject.map {
            values -> values.filter { it.key in keys }
    }

    override fun find(query: Map<String, Any>): Observable<List<V>> = subject

    override fun findOne(key: K): Single<V> = find(setOf(key)).flatMapIterable { it }.firstOrError()

    override fun keys(): Observable<List<K>> = subject.map { values -> values.map { it.key } }

    override fun size(): Observable<Int> = Observable.combineLatest<Int, List<V>, Int>(
        Observable.just(0),
        find(),
        BiFunction { default, current -> max(default, current.size) }
    )

    override fun update(values: Collection<V>): Single<Int> {
        val updates = values.subtract(model.values)

        if (updates.isNotEmpty()) {
            model.putAll(updates.map { it.key to it }.toMap())
            setChanged()
            notifyObservers()
        }

        return Single.just(updates.size)
    }

    override fun updateOne(value: V) = update(listOf(value))

    override fun notifyObservers(arg: Any?) {
        super.notifyObservers(arg)
        subject.onNext(model.values.toList())
    }

}
