package com.first.retflix.view.watchlist.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.first.retflix.R
import com.first.retflix.model.database.Watchinglist
import com.first.retflix.utils.Constans
import kotlinx.android.synthetic.main.item_watchlist.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class WatchlistRecycleviewAdapter(
    private val mValues: List<Watchinglist>,
    private val onCLick: onClickListener
) : RecyclerView.Adapter<WatchlistRecycleviewAdapter.ViewHolder>() {

    interface onClickListener {
        fun itemOnClick(item: Watchinglist)
        fun detailMovie(item: Watchinglist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_watchlist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]

        Glide.with(holder.mView.context)
            .load(Constans.IMAGE_URL_THEMOVIEDBw500 + item.movieBackdrop)
            .apply(RequestOptions().error(R.drawable.poster))
            .into(holder.backdropMovie)

        holder.judulMovie.text = item.movieTitle
        holder.ratingMovie.text = item.movieRate.toString()

        holder.backdropMovie.onClick {
            onCLick.itemOnClick(item)
        }

        holder.detailMovie.onClick {
            onCLick.detailMovie(item)
        }
    }


    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val backdropMovie: ImageView = mView.watchlistBackdrop
        val judulMovie: TextView = mView.watchlistJudul
        val ratingMovie: TextView = mView.watchlistRating
        val detailMovie:ImageView = mView.watchlistDetail

        override fun toString(): String {
            return super.toString()
        }
    }


}