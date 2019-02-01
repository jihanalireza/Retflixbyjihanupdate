package com.first.retflix.view.watchlist

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.first.retflix.R
import com.first.retflix.model.database.AppDatabase
import com.first.retflix.model.database.Watchinglist
import com.first.retflix.model.themoviewdb.trending.ResultsItem
import com.first.retflix.view.detailmovie.DetailMovieActivity
import com.first.retflix.view.watchlist.adapter.WatchlistRecycleviewAdapter
import kotlinx.android.synthetic.main.activity_watchlist.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.*
import org.jetbrains.anko.coroutines.experimental.bg

class WatchlistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watchlist)
        getData()
    }

    fun getData(){
        async(UI){
            val dataWatchlist = bg {
                AppDatabase.getDatabase(this@WatchlistActivity).WatchlistDao().getAll()
            }

            showData(dataWatchlist.await())
        }
    }

    private fun showData(await: List<Watchinglist>) {
        watchlistRecycleview.adapter = WatchlistRecycleviewAdapter(await,object :WatchlistRecycleviewAdapter.onClickListener{
            override fun detailMovie(item: Watchinglist) {
                startActivity<DetailMovieActivity>("movieId" to item.movieId.toString())
            }

            override fun itemOnClick(item: Watchinglist) {
                alert {
                    title = "Konfirmasi hapus watchlist"
                    message = "Yakin akan hapus watchlist ${item.movieTitle}"
                    yesButton {
                        removeWatchlist(item.uid)
                    }
                    noButton {
                        dialog -> dialog.dismiss()
                    }
                }.show()
            }

        })

        watchlistRecycleview.layoutManager = LinearLayoutManager(this)
    }

    private fun removeWatchlist(uid: Int?) {
        val watchList = Watchinglist()
        watchList.uid = uid
        async(UI){
            bg {
                AppDatabase.getDatabase(this@WatchlistActivity).WatchlistDao().delete(watchList)
                getData()
            }
        }
        toast("Remove watchlist...")

    }



}
