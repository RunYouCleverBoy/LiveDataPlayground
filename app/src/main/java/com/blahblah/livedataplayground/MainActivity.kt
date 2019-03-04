package com.blahblah.livedataplayground

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blahblah.livedataplayground.fragments.GalleryFragment
import com.blahblah.livedataplayground.fragments.SynopsisFragment
import com.blahblah.livedataplayground.model.OneMovieEntity
import com.blahblah.livedataplayground.utils.CoroutineWrapper
import com.blahblah.livedataplayground.viewmodel.MoviesViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var model: MoviesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = MoviesViewModel.init(application)
        setContentView(R.layout.activity_main)
        setupTheGalleryFragment()
    }

    private fun setupTheGalleryFragment() {
        val galleryFragment =
            supportFragmentManager.findFragmentByTag(getString(R.string.galleryFragmentTagName)) as? GalleryFragment
        galleryFragment?.setupWith(viewModel = model, interactionFunction = { what: GalleryFragment.Interaction ->
            when (what) {
                is GalleryFragment.MovieClicked -> showMovieDetails(what.oneMovieEntity)
            }
        })
    }

    private fun showMovieDetails(oneMovieEntity: OneMovieEntity) {
        CoroutineWrapper.launchUI {
            val fragment = supportFragmentManager.findFragmentByTag(SynopsisFragment.TAG) as? SynopsisFragment
                ?: SynopsisFragment()
                    .also {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.synopsisContainer, it, SynopsisFragment.TAG)
                            .addToBackStack(SynopsisFragment.TAG)
                            .commit()
                    }
            fragment.applyEntity(oneMovieEntity)
        }
    }
}
