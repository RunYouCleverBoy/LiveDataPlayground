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
import com.blahblah.livedataplayground.viewmodel.MoviesViewModel

/**
 * Description:
 * Created by shmuel on 2.3.19.
 */
class GalleryFragment : Fragment() {
    private val viewModel by lazy { MoviesViewModel.instance }
    var interactionLambda: (what: Interaction) -> Unit = {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val orientation = activity?.resources?.configuration?.orientation
        val numColumns = when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> 8
            Configuration.ORIENTATION_PORTRAIT -> 4
            else -> 4
        }
        val view = inflater.inflate(R.layout.gallery_layout, container, false)
        val gallery: RecyclerView = view.findViewById(R.id.galleryRecycler)
        gallery.adapter = GalleryFragmentAdapter(viewModel ?: return null, this::onWantMore)
        { item -> interactionLambda(MovieClicked(item)) }
        gallery.layoutManager = GridLayoutManager(activity, numColumns)
        return view
    }

    interface Interaction
    data class MovieClicked(val oneMovieEntity: OneMovieEntity) : Interaction

    private fun onWantMore(position: IntRange) {
        viewModel?.fetch(position)
    }
}