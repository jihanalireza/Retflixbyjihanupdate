package com.first.retflix.model.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
class Watchinglist {
    @PrimaryKey(autoGenerate = true)
    var uid: Int? = 0
    @ColumnInfo(name = "movie_id")
    var movieId: Int? = null
    @ColumnInfo(name = "movie_title")
    var movieTitle: String? = null
    @ColumnInfo(name = "movie_backdrop")
    var movieBackdrop: String? = null
    @ColumnInfo(name = "movie_rate")
    var movieRate: Double? = 0.0
}