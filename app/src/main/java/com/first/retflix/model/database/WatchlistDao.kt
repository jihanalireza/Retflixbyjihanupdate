package com.first.retflix.model.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface WatchlistDao {
    @Query("SELECT * FROM watchinglist")
    fun getAll(): List<Watchinglist>

    @Query("SELECT * FROM watchinglist WHERE movie_id = :movieId")
    fun findById(movieId :Int):List<Watchinglist>

    @Insert
    fun insertAll(vararg watchinglists: Watchinglist)

    @Delete
    fun delete(watchinglist: Watchinglist)
}