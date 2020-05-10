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

package com.jpventura.popularmovies.app.di

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.jpventura.popularmovies.episodes.vm.EpisodesViewModel
import com.jpventura.popularmovies.series.vm.SeriesViewModel
import org.kodein.di.DKodein
import org.kodein.di.KodeinAware
import org.kodein.di.direct
import org.kodein.di.generic.instance

class ViewModelFactory(private val injector: DKodein) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = with(modelClass) {
        when {
            isAssignableFrom(EpisodesViewModel::class.java) -> EpisodesViewModel(
                injector.instance(),
                injector.instance()
            )
            isAssignableFrom(SeriesViewModel::class.java) -> SeriesViewModel(injector.instance())
            else -> throw IllegalArgumentException("Model not supported: ${modelClass.simpleName}")
        }
    } as T
}

inline fun <reified VM : ViewModel, T> T.viewModel(): Lazy<VM> where T : KodeinAware, T : Fragment {
    return lazy { ViewModelProviders.of(requireActivity(), direct.instance()).get(VM::class.java) }
}

inline fun <reified VM : ViewModel, T> T.viewModel(): Lazy<VM> where T : KodeinAware, T : FragmentActivity {
    return lazy { ViewModelProviders.of(this, direct.instance()).get(VM::class.java) }
}

inline fun <reified VM : ViewModel, T> T.viewModel(): Lazy<VM> where T : KodeinAware, T : AppCompatActivity {
    return lazy { ViewModelProviders.of(this, direct.instance()).get(VM::class.java) }
}
