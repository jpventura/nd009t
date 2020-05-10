package com.jpventura.popularmovies.series.ui

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jpventura.core.android.ktx.inflate
import com.jpventura.core.android.ui.RecyclerViewArrayAdapter
import com.jpventura.domain.bean.Show
import com.jpventura.popularmovies.R
import kotlinx.android.synthetic.main.item_series.view.*

class SeriesAdapter : RecyclerViewArrayAdapter<Show, SeriesAdapter.ViewHolder>() {

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
        val series = getItem(position)
        holder.itemView.tag = series

        Glide.with(holder.poster)
            .load(series.poster)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .circleCrop()
            .into(holder.poster)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = parent.inflate(R.layout.item_series)

        item.setOnClickListener {
            (item.tag as? Show)?.let {
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
        val poster: ImageView = item.image_item_poster
    }

}
