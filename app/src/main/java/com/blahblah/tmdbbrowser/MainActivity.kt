package com.blahblah.tmdbbrowser

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blahblah.tmdbbrowser.fragments.GalleryFragment
import com.blahblah.tmdbbrowser.fragments.SynopsisFragment
import com.blahblah.tmdbbrowser.model.OneMovieEntity
import com.blahblah.tmdbbrowser.utils.CoroutineWrapper
import com.blahblah.tmdbbrowser.viewmodel.MoviesViewModel

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
        galleryFragment?.autoSelectFirst = isTablet()
        galleryFragment?.setupWith(viewModel = model, interactionFunction = { what: GalleryFragment.Interaction ->
            when (what) {
                is GalleryFragment.MovieClicked -> showMovieDetails(what.oneMovieEntity)
            }
        })
    }

    private fun isTablet() = when (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) {
        Configuration.SCREENLAYOUT_SIZE_LARGE -> true
        Configuration.SCREENLAYOUT_SIZE_XLARGE -> true
        else -> false
    }

    private fun showMovieDetails(oneMovieEntity: OneMovieEntity) {
        CoroutineWrapper.launchUI {
            val fragment = supportFragmentManager.findFragmentByTag(SynopsisFragment.TAG) as? SynopsisFragment
                ?: SynopsisFragment()
                    .also {
                        val transaction = supportFragmentManager.beginTransaction()
                            .replace(R.id.synopsisContainer, it, SynopsisFragment.TAG)
                        if (!isTablet()) {
                            transaction.addToBackStack(SynopsisFragment.TAG)
                        }
                        transaction.commit()

                    }
            fragment.applyEntity(oneMovieEntity)
        }
    }
}
