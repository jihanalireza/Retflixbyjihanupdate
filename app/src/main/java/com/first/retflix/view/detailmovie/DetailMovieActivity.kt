package com.first.retflix.view.detailmovie

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.first.retflix.R
import com.first.retflix.model.database.AppDatabase
import com.first.retflix.model.database.Watchinglist
import com.first.retflix.model.themoviewdb.detailmovie.ResultDetailMovie
import com.first.retflix.presenter.movie.DetailMoviePresenter
import com.first.retflix.presenter.movie.DetailMovieView
import com.first.retflix.utils.Constans
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_movie.*
import org.jetbrains.anko.toast
import com.google.android.youtube.player.YouTubeStandalonePlayer
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk25.coroutines.onClick


class DetailMovieActivity : AppCompatActivity(), DetailMovieView {

    var videoYtKey: ArrayList<String>? = ArrayList()
    var dataDetail: ResultDetailMovie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)
        val movieId = intent.getStringExtra("movieId")

        val detailPresenter = DetailMoviePresenter(this)
        detailPresenter.getDetailMovie(movieId.toInt())

        btnPlay.onClick {
            val intent = YouTubeStandalonePlayer.createVideoIntent(
                this@DetailMovieActivity, Constans.API_KEY_GOOGLE,
                videoYtKey?.get(0), 0, true, false
            )
            startActivity(intent)
        }

        btnPlay2.onClick {
            val intent = YouTubeStandalonePlayer.createVideosIntent(
                this@DetailMovieActivity, Constans.API_KEY_GOOGLE,
                videoYtKey, 0, 0, true, false
            )
            startActivity(intent)
        }

        btnAddWatchlist.onClick{
            if (dataDetail?.id != null) {
                async(UI) {
                    val getWatch = bg {
                        AppDatabase.getDatabase(this@DetailMovieActivity).WatchlistDao().findById(dataDetail?.id ?: 0)
                    }

                    addToWatchlist(getWatch.await())
                }
            }else{
                toast("Sedang mengambil data detail...")
            }
        }


    }

    private fun addToWatchlist(data: List<Watchinglist>){
        if(data.size > 0){
            toast("Sudah ditambahkan ke watchlist")
        }else{
            val watchinglist = Watchinglist()
            watchinglist.uid = dataDetail?.id
            watchinglist.movieTitle = dataDetail?.originalTitle
            watchinglist.movieBackdrop = dataDetail?.backdropPath
            watchinglist.movieId = dataDetail?.id
            watchinglist.movieRate = dataDetail?.voteAverage

            async(UI){
                bg {
                    AppDatabase.getDatabase(this@DetailMovieActivity).WatchlistDao().insertAll(watchinglist)
                }
            }

            toast("Berhasil ditambahkan ke watchlist")
        }
    }


    override fun onResponseDetail(body: ResultDetailMovie?) {
        dataDetail = body
        showDetail(body)
    }

    override fun onFailurDetail(localizedMessage: String) {
        toast(localizedMessage)
    }

    private fun showDetail(body: ResultDetailMovie?) {
        val vote = body?.voteCount.toString()
        val releaseMovie: List<String>? = body?.releaseDate.toString().split("-")
        val year = releaseMovie?.get(0)
        var no = 0
        (body?.videos?.results)?.forEach { i ->
            videoYtKey?.add(no++, i?.key.toString())
        }

        Glide.with(this).load(Constans.IMAGE_URL_THEMOVIEDBoriginal + body?.backdropPath)
            .apply(RequestOptions().error(R.drawable.poster))
            .into(backdropMovie)

        Glide.with(this).load(Constans.IMAGE_URL_THEMOVIEDBw200 + body?.posterPath)
            .apply(RequestOptions().error(R.drawable.poster))
            .into(posterMovie)

        judulMovie.text = body?.originalTitle
        ratingMovie.text = body?.voteAverage.toString()
        voteCount.text = "$vote Votes"
        descriptionMovie.text = body?.overview
        ReleaseMovie.text = "$year"

    }
}
