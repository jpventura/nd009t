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

package com.jpventura.popularmovies.series.vm

import com.jpventura.core.android.lifecycle.Failure
import com.jpventura.core.android.lifecycle.Loading
import com.jpventura.core.android.lifecycle.ObservableViewModel
import com.jpventura.core.android.lifecycle.Success
import com.jpventura.domain.model.TelevisionSeriesModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import timber.log.Timber

class SeriesViewModel(
    private val series: TelevisionSeriesModel.Series
) : ObservableViewModel() {

    private val subject: Subject<String> = PublishSubject.create()

    fun findSeries(page: Int? = 1, query: String? = null) {
        subject.onNext(query ?: "")

        disposables.add(
            if (query.isNullOrBlank()) {
                series.find(query = mapOf("page" to page))
            } else {
                series.find(query = mapOf("page" to page, "query" to query))
            }.doOnSubscribe {
                state = Loading
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    state = Success(result = it)
                    Timber.w(state.toString())
                }, {
                    state = Failure(throwable = it)
                    Timber.e(it)
                })
        )
    }

    fun search(page: Int? = 1, name: String? = null) {
        subject.onNext(name ?: "")

        disposables.add(
            series.find(
                query = mapOf("page" to page, "name" to name)
            ).doOnSubscribe {
                state = Loading
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    state = Success(result = it)
                    Timber.w(state.toString())
                }, {
                    state = Failure(throwable = it)
                    Timber.e(it)
                })
        )
    }
}
