package com.blahblah.tmdbbrowser.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import com.blahblah.tmdbbrowser.model.MoviesDatabase
import com.blahblah.tmdbbrowser.model.OneMovieEntity
import com.blahblah.tmdbbrowser.model.TMDBApi
import com.blahblah.tmdbbrowser.utils.CoroutineWrapper
import com.blahblah.tmdbbrowser.utils.SimpleObserver
import com.blahblah.tmdbbrowser.utils.contains
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


/**
 * Description: View Model for the app
 * Created by shmuel on 27.2.19.
 */
class MoviesViewModel private constructor(application: Application) : AndroidViewModel(application) {
    private val coroutine = CoroutineWrapper(Executors.newSingleThreadExecutor().asCoroutineDispatcher())
    private val database: MoviesDatabase =
        Room.databaseBuilder(application, MoviesDatabase::class.java, "movies_property_db").build()
    private val tmdbDriver = TMDBApi(application)
    private var maxPage = 0
    private val errorRegistrar = SimpleObserver<ErrorType>()
    data class DataChunk(val firstPosition: Int, val data: List<OneMovieEntity>)

    val moviesListPage: MutableLiveData<DataChunk>

    init {
        val initialData = database.moviesListDao().getAllMovies(0, 10).value ?: listOf()
        database.moviesListDao().getMaxPage().observeForever { page -> maxPage = page ?: 0 }
        moviesListPage = MutableLiveData(DataChunk(0, initialData))
        fetch(0..256)
    }

    var currentRequest: IntRange? = null

    /**
     * Fetch entries by [positionRange]. If not available in the database, fetch from TMDB. Data is returned with the livedata
     *
     * @param positionRange range of positions
     *
     */
    fun fetch(positionRange: IntRange = 0 until PAGE_SIZE) {
        CoroutineWrapper.launchUI {
            if (currentRequest?.contains(positionRange) == true) {
                return@launchUI
            }

            currentRequest = positionRange
            val intervalSize = Math.max(PAGE_SIZE, positionRange.last - positionRange.first + 1)
            val dataFromDb = suspendCoroutine<List<OneMovieEntity>?> { continuation ->
                // Fetch a little bit before, and
                val allMovies = database.moviesListDao().getAllMovies(positionRange.first, intervalSize)
                allMovies.observeForever(object : Observer<List<OneMovieEntity>> {
                    override fun onChanged(t: List<OneMovieEntity>?) {
                        continuation.resume(t)
                        allMovies.removeObserver(this)
                    }
                })
            }

            val data = if (dataFromDb?.size ?: 0 < intervalSize) {
                coroutine.withContext {
                    val data = tmdbDriver.getList(maxPage + 1)
                    if (data.isNotEmpty()) {
                        maxPage++
                    } else if (dataFromDb?.isEmpty() != false) {
                        errorRegistrar.notifyObservers(ErrorType.CANNOT_FETCH_DATA)
                    }
                    data.forEach { item -> database.moviesListDao().insert(item) }
                    data
                }
            } else {
                dataFromDb
            } ?: listOf()


            moviesListPage.postValue(DataChunk(positionRange.first, data))
            currentRequest = null
        }
    }

    fun addOnErrorListener(onErrorListener: (ErrorType) -> Unit) = errorRegistrar.addObserver(onErrorListener)
    fun removeOnErrorListener(handle: Int) = errorRegistrar.removeObserver(handle)

    companion object {
        fun init(application: Application): MoviesViewModel {
            return instance ?: MoviesViewModel(application).also { instance = it }
        }

        var instance: MoviesViewModel? = null
        private const val PAGE_SIZE = 20
    }
}