package com.blahblah.livedataplayground.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.blahblah.livedataplayground.R
import com.blahblah.livedataplayground.model.OneMovieEntity
import com.blahblah.livedataplayground.utils.CoroutineWrapper.Companion.launchUI
import com.blahblah.livedataplayground.utils.clipTo
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CompletableDeferred

/**
 * Description: A fragment where synopsis is displayed
 * Created by shmuel on 3.3.19.
 */
class SynopsisFragment : Fragment() {
    private lateinit var holder: Holder
    private val holderPromise = CompletableDeferred<Holder>()
    private var lastMovieEntry: OneMovieEntity? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.synopsis_view, container, false)
        holder = Holder(view)
        holderPromise.complete(holder)

        // TODO: Migrate it all to parcelable and test if it does not break anything
        savedInstanceState?.let { outState ->
            val oneMovieEntity = OneMovieEntity().apply {
                movieName = outState.getString("movieName", "")
                backdropUri = outState.getString("backDropUri", "")
                synopsis = outState.getString("synoposis", "")
                popularity = outState.getDouble("popularity", 0.0)
                voteAverage = outState.getDouble("voteAverage", 0.0)
            }

            launchUI {
                applyEntity(oneMovieEntity)
            }
        }

        return view
    }

    suspend fun applyEntity(oneMovieEntity: OneMovieEntity) {
        holderPromise.await().apply {
            movieName.text = oneMovieEntity.movieName
            Picasso.get().load(oneMovieEntity.backdropUri).into(backDrop)
            synopsis.text = oneMovieEntity.synopsis
            popularity.text = getString(R.string.popularityTemplate, oneMovieEntity.popularity.toInt())
            ratingBar.rating = transformRating(oneMovieEntity.voteAverage, ratingBar.numStars)
            lastMovieEntry = oneMovieEntity
        }
    }

    private fun transformRating(voteAverage: Double, numStars: Int): Float {
        return (voteAverage * numStars / 10.0 + 0.5).toFloat().clipTo(0, numStars)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(lastMovieEntry?.run {
            outState.putString("movieName", movieName)
            outState.putString("backDropUri", backdropUri)
            outState.putString("synoposis", synopsis)
            outState.putDouble("popularity", popularity)
            outState.putDouble("voteAverage", voteAverage)
            outState
        } ?: outState)
    }

    private class Holder(view: View) {
        var movieName: TextView = view.findViewById(R.id.synopsisMovieName)
        var backDrop: ImageView = view.findViewById(R.id.synopsisMovieBackdrop)
        var synopsis: TextView = view.findViewById(R.id.synopsisMovieSynopsis)
        var popularity: TextView = view.findViewById(R.id.synopsisMoviePopularity)
        var ratingBar: RatingBar = view.findViewById(R.id.ratingBar)
    }

    companion object {
        const val TAG = "SynopsisFragment"
    }
}
