package com.blahblah.livedataplayground.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.blahblah.livedataplayground.R
import com.blahblah.livedataplayground.model.OneMovieEntity
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CompletableDeferred

/**
 * Description:
 * Created by shmuel on 3.3.19.
 */
class SynopsisFragment : Fragment() {
    private lateinit var holder: Holder
    private val holderPromise = CompletableDeferred<Holder>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.synopsis_view, container, false)
        holder = Holder(view)
        holderPromise.complete(holder)
        return view
    }

    suspend fun applyEntity(oneMovieEntity: OneMovieEntity) {
        holderPromise.await().apply {
            movieName.text = oneMovieEntity.movieName
            Picasso.get().load(oneMovieEntity.backdropUri).into(backDrop)
            synopsis.text = oneMovieEntity.synopsis
            popularity.text = getString(R.string.popularityTemplate, oneMovieEntity.popularity.toInt())
        }
    }

    private class Holder(view: View) {
        var movieName: TextView = view.findViewById(R.id.synopsisMovieName)
        var backDrop: ImageView = view.findViewById(R.id.synopsisMovieBackdrop)
        var synopsis: TextView = view.findViewById(R.id.synopsisMovieSynopsis)
        var popularity: TextView = view.findViewById(R.id.synopsisMoviePopularity)
    }

    companion object {
        const val TAG = "SynopsisFragment"
    }
}