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

import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jpventura.core.android.ktx.inflate
import com.jpventura.core.android.ui.RecyclerViewArrayAdapter
import com.jpventura.domain.bean.Episode
import com.jpventura.popularmovies.R
import kotlinx.android.synthetic.main.item_episodes.view.*

class EpisodesAdapter : RecyclerViewArrayAdapter<Episode, EpisodesAdapter.ViewHolder>() {

    private var onItemClickListener: ((
        parent: ViewGroup,
        view: View?,
        position: Int,
        id: Long
    ) -> Unit)? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long = getItem(position).key.hashCode().toLong()

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val episode = getItem(position)

        holder.itemView.tag = episode

        holder.title.text = holder.itemView.context.getString(
            R.string.label_item_episode,
            episode.season,
            episode.number,
            episode.name
        )

        holder.body.text = if (episode.summary.isBlank()) {
            holder.itemView.context.getString(R.string.body_episodes_summary)
        } else {
            Html.fromHtml(episode.summary)
        }

        Glide.with(holder.poster)
            .load(episode.image)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.poster)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = parent.inflate(R.layout.item_episodes)

        item.setOnClickListener {
            (item.tag as? Episode)?.let {
                val position = getPosition(it)
                onItemClickListener?.invoke(parent, item, position, getItemId(position))
            }
        }

        return ViewHolder(item)
    }

    fun setOnItemClickListener(
        onItemClickListener: ((parent: ViewGroup, item: View?, position: Int, id: Long) -> Unit)?
    ) {
        this.onItemClickListener = onItemClickListener
    }

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val poster: ImageView = item.mtrl_list_item_icon
        val title: TextView = item.mtrl_list_item_text
        val body: TextView = item.mtrl_list_item_secondary_text
    }

}
