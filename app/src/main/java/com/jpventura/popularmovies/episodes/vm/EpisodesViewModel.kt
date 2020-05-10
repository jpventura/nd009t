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

package com.jpventura.popularmovies.episodes.vm

import com.jpventura.core.android.lifecycle.Failure
import com.jpventura.core.android.lifecycle.Loading
import com.jpventura.core.android.lifecycle.ObservableViewModel
import com.jpventura.core.android.lifecycle.Success
import com.jpventura.domain.bean.Show
import com.jpventura.domain.model.TelevisionSeriesModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

class EpisodesViewModel(
    private val series: TelevisionSeriesModel.Series,
    private val episodes: TelevisionSeriesModel.Episodes
) : ObservableViewModel() {

    fun toggleFavorite() {
        show?.let {
            series.toggleOne(it.key, isFavorite)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it
                }, {
                    Timber.w(it)
                })
        }
    }

    var show: Show? = null
        set(value) {
            field = value
            toggleFavorite()
            notifyChange()
        }

    var isFavorite: Boolean
        get() = show?.favorite ?: false
        set(value) {
            show = show?.copy(favorite = value)
        }

    val rating: Float
        get() = show?.rating?.toFloat() ?: 0F

    fun findEpisodes(show: Show, page: Int? = 1) {
        disposables.add(episodes.find(show)
            .doOnSubscribe {
                state = Loading
            }
            .subscribeOn(Schedulers.io())
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
