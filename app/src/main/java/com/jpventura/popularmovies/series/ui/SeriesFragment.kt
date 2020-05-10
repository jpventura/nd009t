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

package com.jpventura.popularmovies.series.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import com.jpventura.core.android.ktx.toObservable
import com.jpventura.popularmovies.R
import com.jpventura.popularmovies.app.di.viewModel
import com.jpventura.popularmovies.app.ui.InjectedFragment
import com.jpventura.popularmovies.databinding.FragmentSeriesBinding
import com.jpventura.popularmovies.series.vm.SeriesViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SeriesFragment : InjectedFragment() {

    lateinit var adapter: SeriesAdapter
    lateinit var binding: FragmentSeriesBinding
    lateinit var searchView: SearchView

    private val disposables = CompositeDisposable()
    private val vm: SeriesViewModel by viewModel()
    private var query: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSeriesBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = vm

        adapter = SeriesAdapter()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.dashboard, menu)
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setQuery(null, false)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        onPrepareSearchView(menu)
        super.onPrepareOptionsMenu(menu)
    }

    private fun onPrepareSearchView(menu: Menu) {
        searchView
            .toObservable()
            .debounce(400, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                if (query != it) {
                    adapter.clear()
                }
                query = it
            }
            .subscribe {
                vm.findSeries(query = query)
            }.let {
                disposables.add(it)
            }
    }

    companion object {

        const val TAG = "SeriesFragment"

        fun newInstance() = SeriesFragment()

    }

}
