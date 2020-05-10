package com.jpventura.popularmovies.app.ui

import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import android.widget.ToggleButton
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jpventura.core.android.ktx.visible
import com.jpventura.core.android.lifecycle.*
import com.jpventura.domain.bean.Episode
import com.jpventura.domain.bean.Show
import com.jpventura.popularmovies.episodes.ui.EpisodesAdapter
import com.jpventura.popularmovies.series.ui.SeriesAdapter
import kotlin.math.round

@BindingAdapter("state")
fun setRecyclerView(view: RecyclerView, state: State) {
    if (state !is Success) return

    when (val adapter = view.adapter) {
        is EpisodesAdapter -> {
            adapter.addAll(state.result.map { it as Episode }.toList())
        }
        is SeriesAdapter -> {
            adapter.addAll(state.result.map { it as Show }.toList())
        }
    }
}

@BindingAdapter("state")
fun setProgressBar(view: ProgressBar, state: State) {
    when (state) {
        Loading -> {
            view.visible = true
        }
        else -> {
            view.visible = false
        }
    }
}

@BindingAdapter("isFavorite")
fun setToggleButton(view: ToggleButton, isChecked: Boolean) {
    view.isChecked = isChecked
}

@BindingAdapter("stars")
fun setRatingBar(view: RatingBar, rating: Float) {
    view.rating = (5*rating/10.0).toFloat()
}

@BindingAdapter("state")
fun setTextView(view: TextView, state: State) {
    when (state) {
        is Uninitialized -> {
            view.visible = true
        }
        is Loading -> {
            view.visible = false
        }
        is Success -> {
            view.visible = state.result.isEmpty()
        }
        is Failure -> {
            view.visible = true
        }
    }
}
