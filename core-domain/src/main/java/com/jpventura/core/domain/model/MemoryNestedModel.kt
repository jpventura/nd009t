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
import io.reactivex.subjects.BehaviorSubject
import kotlin.math.max

abstract class MemoryNestedModel<P, K, V : NestedBean<P, K>>(
    model: Collection<V> = emptyList()
) : java.util.Observable(), NestedPersistedModel<P, K, V> {

    private val model: MutableMap<String, V> = model
        .map { "${it.parentKey}/${it.key}" to it }
        .toMap()
        .toMutableMap()

    private val subject = BehaviorSubject.create<List<V>>()

    init {
        super.setChanged()
        subject.onNext(this.model.values.toList())
    }

    override fun <U : Bean<P>> find(parent: U): Observable<List<V>> {
        return find(query = emptyMap())
            .flatMapIterable { it }
            .filter { it.parentKey == parent.key  }
            .toList()
            .toObservable()
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

    override fun containsKey(key: String) = Single.just(key in model.keys)

    override fun containsKeys(keys: Collection<String>) =
        Single.just(keys.subtract(model.keys).isEmpty())

    override fun containsValue(value: V) = containsValues(setOf(value))

    override fun containsValues(values: Collection<V>) = Completable.create { emitter ->
        val difference = values.toSet().subtract(model.values)

        if (difference.isEmpty()) {
            emitter.onComplete()
        } else {
            emitter.onError(NoSuchElementException("No such element: ${difference.first()}"))
        }
    }

    override fun create(values: Collection<V>): Single<List<String>> {
        val entries = values.map { compoundKey(it) to it }
        val collision = entries.intersect(model.entries)

        if (collision.isNotEmpty()) {
            val value = collision.first()
            return Single.error(IllegalStateException("Value already contained: $value"))
        }

        model.putAll(entries.toMap())
        setChanged()
        notifyObservers()

        return Single.just(values.map { compoundKey(it) })
    }

    override fun createOne(value: V): Single<String> = create(listOf(value)).map { it.first() }

    override fun createOrUpdate(values: Collection<V>): Single<List<String>> {
        val updates = values.filter { !model.values.contains(it) }.map { compoundKey(it) to it }.toMap()
        val keys = values.map { compoundKey(it) }.toList()

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

    override fun destroy(keys: Collection<String>): Single<List<String>> {
        val destroyed = keys.mapNotNull { model.remove(it) }.map { compoundKey(it) }

        if (destroyed.isNotEmpty()) {
            setChanged()
            notifyObservers()
        }

        return Single.just(destroyed)
    }

    override fun destroyOne(key: String): Single<String> = destroy(listOf(key)).map { it.first() }

    override fun find(keys: Collection<String>): Observable<List<V>> = subject.map {
            values -> values.filter { compoundKey(it) in keys }
    }

    override fun find(query: Map<String, Any?>): Observable<List<V>> = subject

    override fun findOne(key: String): Single<V> = find(setOf(key)).flatMapIterable { it }.firstOrError()

    override fun keys(): Observable<List<String>> = subject.map { values -> values.map { compoundKey(it) } }

    override fun size(): Observable<Int> = Observable.combineLatest<Int, List<V>, Int>(
        Observable.just(0),
        find(),
        BiFunction { default, current -> max(default, current.size) }
    )

    override fun update(values: Collection<V>): Single<Int> {
        val updates = values.subtract(model.values)

        if (updates.isNotEmpty()) {
            model.putAll(updates.map { compoundKey(it) to it }.toMap())
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

    private fun compoundKey(value: V): String = "${value.parentKey}/${value.key}"

}
