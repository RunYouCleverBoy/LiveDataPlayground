package com.blahblah.livedataplayground.fragments.adapters

import android.net.Uri
import android.util.LruCache
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.blahblah.livedataplayground.R
import com.blahblah.livedataplayground.model.OneMovieEntity
import com.blahblah.livedataplayground.viewmodel.MoviesViewModel
import com.squareup.picasso.Picasso

/**
 * Description:
 * Created by shmuel on 2.3.19.
 */
class GalleryFragmentAdapter(viewModel: MoviesViewModel) :
    RecyclerView.Adapter<GalleryFragmentAdapter.EntryViewHolder>() {
    private val cache = LruCache<Int, OneMovieEntity>(100)
    private var dataSize = 0

    init {
        viewModel.moviesListPage.observeForever { chunk ->
            chunk.data.forEachIndexed { i, entity -> cache.put(i + chunk.firstPosition, entity) }
            dataSize = Math.max(dataSize, chunk.firstPosition + chunk.data.size)
            notifyDataSetChanged()
        }
        viewModel.fetch()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gallery_item, parent, false) as ImageView
        return EntryViewHolder(view)
    }

    override fun getItemCount() = dataSize
    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        // TODO: Glide
        val url = Uri.parse("https://image.tmdb.org/t/p/w185")
            .buildUpon().appendPath(cache[position].posterUri).build()
            .toString()
        Picasso.get().load(url).into(holder.imageView)
    }

    class EntryViewHolder(view: ImageView) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view
    }
}