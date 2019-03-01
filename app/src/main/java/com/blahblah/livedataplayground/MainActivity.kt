package com.blahblah.livedataplayground

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.blahblah.livedataplayground.model.OneMovieEntity
import com.blahblah.livedataplayground.model.TMDBApi
import com.blahblah.livedataplayground.utils.CoroutineWrapper
import com.blahblah.livedataplayground.viewmodel.MoviesViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var holder: Holder
    private val coroutine = CoroutineWrapper()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        TMDBApi(this)
        activateButton.setOnClickListener {
            ViewModelProviders.of(this@MainActivity).get(MoviesViewModel::class.java)
//            holder = Holder()
//            holder.viewModel.moviesListLiveData.observe(this, Observer { moviesList -> onListChanged(moviesList) })
        }

        refreshButton.setOnClickListener {
            //            coroutine.launch { holder.viewModel.refreshData() }
        }
    }

    private fun onListChanged(moviesList: List<OneMovieEntity>) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private inner class Holder {
        val viewModel = ViewModelProviders.of(this@MainActivity).get(MoviesViewModel::class.java)
    }
}
