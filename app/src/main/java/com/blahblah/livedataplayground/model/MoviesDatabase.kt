package com.blahblah.livedataplayground.model

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Description:
 * Created by shmuel on 27.2.19.
 */
@Database(entities = [OneMovieEntity::class], version = 1)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun discoverDao(): EntitiesDao
}