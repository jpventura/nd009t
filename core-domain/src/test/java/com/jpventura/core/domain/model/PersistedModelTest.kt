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
import io.reactivex.observers.TestObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

abstract class PersistedModelTest<K, V : Bean<K>>(
    private val samples: Map<K, V>
) {

    abstract val model: PersistedModel<K, V>

    private val testScheduler = TestScheduler()

    @Before
    fun setUp() {
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }
        RxJavaPlugins.setIoSchedulerHandler { testScheduler }
    }

    @Test
    fun shouldNotClearElements() {
        model.size()
            .firstOrError()
            .test()
            .assertValue { it == 0 }
            .dispose()
    }

    @Test
    fun shouldListenUpdatesIndefinitely() {
        val testObserver = TestObserver<List<V>>()

        val values = samples.values.toList()

        val events = listOf(
            emptyList(),
            values,
            values.subList(1, values.size)
        )

        model.find().subscribeOn(testScheduler).subscribe(testObserver)
        testScheduler.advanceTimeBy(1, TimeUnit.MILLISECONDS)

        model.createOrUpdate(values)
        testScheduler.advanceTimeBy(1, TimeUnit.MILLISECONDS)

        model.destroyOne(values.first().key)
        testScheduler.advanceTimeBy(1, TimeUnit.MILLISECONDS)

        testObserver.assertValues(*events.toTypedArray())
            .assertNoErrors()
            .dispose()

        tearDownModel()
    }

    @Test
    fun shouldCreateElements() {
        model.create(samples.values)
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValue(samples.keys.toList())
            .dispose()

        tearDownModel()
    }

    @Test
    fun shouldCreateOneElement() {
        val value = samples.values.first()

        model.createOne(value)
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValue { it == value.key }
            .dispose()

        tearDownModel()
    }

    @Test
    fun shouldDestroyAllElements() {
        setupModel()

        model.destroy(samples.keys)
            .flatMap {
                model.containsKeys(samples.keys)
            }
            .test()
            .assertNoErrors()
            .assertComplete()
            .dispose()

        tearDownModel()
    }

    @Test
    fun shouldFindNoElements() {
        model.clear()
            .flatMapObservable { model.find() }
            .firstOrError()
            .test()
            .assertValue { it.isEmpty() }
            .assertComplete()
            .assertNoErrors()
            .dispose()
    }

    @Test
    fun shouldFindNoKeys() {
        model.keys()
            .firstOrError()
            .test()
            .assertValue { it.isEmpty() }
            .assertComplete()
            .assertNoErrors()
            .dispose()
    }

    @Test
    fun shouldFindKeys() {
        setupModel()

        model.keys()
            .firstOrError()
            .test()
            .assertValue(samples.keys.toList())
            .assertComplete()
            .assertNoErrors()
            .dispose()

        tearDownModel()
    }

    @Test
    fun shouldBeEmpty() {
        tearDownModel()
    }

    @After
    fun tearDown() {
        tearDownModel()
        RxJavaPlugins.reset()
    }

    private fun setupModel() {
        model.createOrUpdate(samples.values)
            .flatMapObservable { model.find() }
            .firstOrError()
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValue(samples.values.toList())
            .dispose()
    }

    private fun tearDownModel() {
        model.clear()
            .flatMapObservable {
                model.size()
            }
            .firstOrError()
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValue(0)
            .dispose()
    }

}
