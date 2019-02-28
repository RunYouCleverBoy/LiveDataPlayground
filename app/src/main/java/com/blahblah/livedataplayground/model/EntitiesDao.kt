package com.blahblah.livedataplayground.model

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Description:
 * Created by shmuel on 27.2.19.
 */
@Dao
interface EntitiesDao {
    @Insert
    fun insert(oneMovieEntity: OneMovieEntity)

    @Update
    fun update(oneMovieEntity: OneMovieEntity)

    @Delete
    fun delete(oneMovieEntity: OneMovieEntity)

    @Query("SELECT * FROM Discover where movieName like :name")
    fun findMovieByName(name: String): LiveData<List<OneMovieEntity>>

    @Query("SELECT * FROM Discover")
    fun getAllMovies(): LiveData<List<OneMovieEntity>>
}