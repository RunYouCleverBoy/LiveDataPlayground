package com.blahblah.tmdbbrowser.model

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Description: Movies database parent
 * Created by shmuel on 27.2.19.
 */
@Database(entities = [OneMovieEntity::class], version = 1)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun moviesListDao(): EntitiesDao
}