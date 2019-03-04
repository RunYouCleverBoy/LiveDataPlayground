package com.blahblah.livedataplayground.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blahblah.livedataplayground.R
import com.blahblah.livedataplayground.fragments.adapters.GalleryFragmentAdapter
import com.blahblah.livedataplayground.model.OneMovieEntity
import com.blahblah.livedataplayground.utils.CoroutineWrapper
import com.blahblah.livedataplayground.viewmodel.MoviesViewModel
import kotlinx.coroutines.CompletableDeferred

/**
 * Description:
 * Created by shmuel on 2.3.19.
 */
class GalleryFragment : Fragment() {
    interface Interaction
    data class MovieClicked(val oneMovieEntity: OneMovieEntity) : Interaction

    private var interactionLambda: (what: Interaction) -> Unit = {}

    private data class SetupData(val viewModel: MoviesViewModel, val interactionListener: (Interaction) -> Unit)

    private val setupBlocker = CompletableDeferred<SetupData>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val orientation = activity?.resources?.configuration?.orientation
        val numColumns = when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> 6
            Configuration.ORIENTATION_PORTRAIT -> 3
            else -> 4
        }
        val view = inflater.inflate(R.layout.gallery_layout, container, false)
        CoroutineWrapper.launchUI {
            val gallery: RecyclerView = view.findViewById(R.id.galleryRecycler)
            val (viewModel, interactionListener) = setupBlocker.await()
            interactionLambda = interactionListener
            gallery.adapter = GalleryFragmentAdapter(lifecycle, viewModel, this@GalleryFragment::onWantMore)
            { item -> interactionLambda(MovieClicked(item)) }
            gallery.layoutManager = GridLayoutManager(activity, numColumns)
        }
        return view
    }

    private fun onWantMore(position: IntRange) {
        CoroutineWrapper.launchUI {
            setupBlocker.await().viewModel.fetch(position)
        }
    }

    fun setupWith(viewModel: MoviesViewModel, interactionFunction: (Interaction) -> Unit) {
        setupBlocker.complete(SetupData(viewModel, interactionFunction))
    }
}