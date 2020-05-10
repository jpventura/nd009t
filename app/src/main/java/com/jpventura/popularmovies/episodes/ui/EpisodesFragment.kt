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

package com.jpventura.popularmovies.episodes.ui

import android.os.Bundle
import android.text.Html
import android.view.*
import androidx.core.content.ContextCompat.getColor
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jpventura.core.android.ui.SpacesItemDecoration
import com.jpventura.domain.bean.schedule
import com.jpventura.domain.bean.show
import com.jpventura.popularmovies.R
import com.jpventura.popularmovies.app.di.viewModel
import com.jpventura.popularmovies.app.ui.InjectedFragment
import com.jpventura.popularmovies.app.ui.activity
import com.jpventura.popularmovies.databinding.FragmentEpisodesBinding
import com.jpventura.popularmovies.episodes.vm.EpisodesViewModel
import kotlinx.android.synthetic.main.fragment_episodes.*

class EpisodesFragment : InjectedFragment() {

    private lateinit var adapter: EpisodesAdapter
    private lateinit var binding: FragmentEpisodesBinding

    private val vm: EpisodesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        adapter = EpisodesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEpisodesBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = vm

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity().setSupportActionBar(toolbar)
        activity().supportActionBar?.setDisplayHomeAsUpEnabled(true)
        collapsing_toolbar.setExpandedTitleColor(getColor(requireActivity(), android.R.color.transparent))

        toggle_favorite.setOnCheckedChangeListener { buttonView, isChecked ->
            vm.isFavorite = isChecked
        }

        recyclerview_episodes.adapter = adapter

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing)
        recyclerview_episodes.addItemDecoration(SpacesItemDecoration(spacingInPixels))

        arguments?.let{
            setSeries(it)
        }
    }

    private fun setSeries(arguments: Bundle) {
        // FIXME: Should be using parcelize
        val show = show {
            id = arguments.getLong("series", 0L)
            genres = arguments.getString("genres", "").split(" ")
            name = arguments.getString("name", "") ?: ""
            poster = arguments.getString("poster", "")
            schedule = schedule {
                time = ""
                days = emptyList()
            }
            summary = arguments.getString("summary") ?: getString(R.string.body_episodes_summary)
        }

        vm.show = show

        Glide.with(this)
            .load(show.poster)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(image_view_background)

        activity().title = show.name

        text_overview.text = Html.fromHtml(show.summary)

        // FIXME: Should be using parcelize
        subtitle.text = show.genres.joinToString(separator = " ")

        vm.findEpisodes(show)
    }

    companion object {

        const val TAG = "EpisodesFragment"

        fun newInstance(arguments: Bundle): EpisodesFragment {
            return EpisodesFragment().apply {
                this.arguments = arguments
            }
        }

    }

}
