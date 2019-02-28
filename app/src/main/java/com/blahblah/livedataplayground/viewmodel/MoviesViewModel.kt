package com.blahblah.livedataplayground.viewmodel

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.blahblah.livedataplayground.model.MoviesDatabase
import com.blahblah.livedataplayground.model.OneMovieEntity

/**
 * Description:
 * Created by shmuel on 27.2.19.
 */
class MoviesViewModel(application: Application) : AndroidViewModel(application) {
    private val database = Room.databaseBuilder(application, MoviesDatabase::class.java, "movies_property_db").build()
    val moviesListLiveData: MutableLiveData<List<OneMovieEntity>> =
        MutableLiveData(database.discoverDao().getAllMovies().value ?: listOf())

    @WorkerThread
    fun refreshData() {
        repeat(1000) { i ->
            OneMovieEntity().apply {
                id = i + System.nanoTime().toInt()
                imageUri =
                    "https://images.pexels.com/photos/1161682/pexels-photo-1161682.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"
                movieName = "Movie-$i"
                synopsis = "Once upon a time there was a movie $movieName"
            }.also { database.discoverDao().insert(it) }
        }
        moviesListLiveData.postValue(database.discoverDao().getAllMovies().value)
    }
}