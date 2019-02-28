package com.blahblah.livedataplayground

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.blahblah.livedataplayground.model.OneMovieEntity
import com.blahblah.livedataplayground.utils.CoroutineWrapper
import com.blahblah.livedataplayground.viewmodel.MoviesViewModel

class MainActivity : AppCompatActivity() {
    private val holder by lazy { Holder() }
    private val coroutine = CoroutineWrapper()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        holder.viewModel.moviesListLiveData.observe(this, Observer { moviesList -> onListChanged(moviesList) })
        coroutine.launch { holder.viewModel.refreshData() }
    }

    private fun onListChanged(moviesList: List<OneMovieEntity>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private inner class Holder {
        val viewModel = ViewModelProviders.of(this@MainActivity).get(MoviesViewModel::class.java)
    }
}
