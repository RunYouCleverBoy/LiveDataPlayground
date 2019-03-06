package com.blahblah.livedataplayground.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blahblah.livedataplayground.R
import com.blahblah.livedataplayground.fragments.adapters.GalleryFragmentAdapter
import com.blahblah.livedataplayground.model.OneMovieEntity
import com.blahblah.livedataplayground.utils.CoroutineWrapper
import com.blahblah.livedataplayground.viewmodel.MoviesViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlin.math.roundToInt

/**
 * Description: Fragment for the gallery
 * Created by shmuel on 2.3.19.
 */
class GalleryFragment : Fragment() {
    interface Interaction
    data class MovieClicked(val oneMovieEntity: OneMovieEntity) : Interaction

    private var interactionLambda: (what: Interaction) -> Unit = {}

    private data class SetupData(val viewModel: MoviesViewModel, val interactionListener: (Interaction) -> Unit)

    private val setupBlocker = CompletableDeferred<SetupData>()
    private val widthPromise = CompletableDeferred<Float>()
    var autoSelectFirst: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.gallery_layout, container, false)

        // To calculate the #columns, it's necessary to know the screen size
        view.viewTreeObserver.addOnGlobalLayoutListener(OnLayoutChange(view))
        CoroutineWrapper.launchUI { setupTheGallery(view) }
        return view
    }

    private suspend fun setupTheGallery(view: View) {
        val numColumns = (widthPromise.await() / resources.getDimension(R.dimen.thumbnailDesiredWidth))
        val gallery: RecyclerView = view.findViewById(R.id.galleryRecycler)
        val (viewModel, interactionListener) = setupBlocker.await()
        interactionLambda = interactionListener
        gallery.adapter =
            GalleryFragmentAdapter(autoSelectFirst, lifecycle, viewModel, this@GalleryFragment::onWantMore)
            { item -> interactionLambda(MovieClicked(item)) }
        gallery.layoutManager = GridLayoutManager(activity, numColumns.roundToInt())
    }

    inner class OnLayoutChange(private val view: View) : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (view.width > 0) {
                widthPromise.complete(view.width.toFloat())
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
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