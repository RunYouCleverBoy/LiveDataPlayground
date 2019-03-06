package com.blahblah.tmdbbrowser.model

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Description: The dao for one movie
 * Created by shmuel on 27.2.19.
 */
@Dao
interface EntitiesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(oneMovieEntity: OneMovieEntity)

    @Update
    fun update(oneMovieEntity: OneMovieEntity)

    @Delete
    fun delete(oneMovieEntity: OneMovieEntity)

    @Query("SELECT * FROM MovieEntry where movieName like :name limit :count offset :fromRow")
    fun findMovieByName(name: String, fromRow: Int = 0, count: Int = 10): LiveData<List<OneMovieEntity>>

    @Query("SELECT * FROM MovieEntry limit :count offset :fromRow")
    fun getAllMovies(fromRow: Int = 0, count: Int = 10): LiveData<List<OneMovieEntity>>

    @Query("SELECT MAX(cameFromPage) FROM MovieEntry")
    fun getMaxPage(): LiveData<Int>
}