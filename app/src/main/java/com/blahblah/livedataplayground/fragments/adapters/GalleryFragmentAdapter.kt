package com.blahblah.livedataplayground.fragments.adapters

import android.util.LruCache
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.blahblah.livedataplayground.R
import com.blahblah.livedataplayground.model.OneMovieEntity
import com.blahblah.livedataplayground.viewmodel.MoviesViewModel
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

/**
 * Description:
 * Created by shmuel on 2.3.19.
 */
class GalleryFragmentAdapter(
    viewModel: MoviesViewModel,
    private val wantMore: (IntRange) -> Unit,
    private val onItemSelected: (OneMovieEntity) -> Unit
) :
    RecyclerView.Adapter<GalleryFragmentAdapter.EntryViewHolder>() {
    private val cache = LruCache<Int, OneMovieEntity>(100)
    private var dataSize = 0
    private var renderer = Picasso.get()

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

    // Request one more to make sure the next page is requested
    override fun getItemCount() = dataSize
    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        val oneMovieEntity = cache[position]
        if (oneMovieEntity != null) {
            val url: String = oneMovieEntity.posterUri
            renderer.load(url).networkPolicy(NetworkPolicy.OFFLINE).into(holder.imageView)
            (position..(position + 10)).find { cache[it] == null }?.let { pos -> wantMore(pos..(pos + 10)) }
            holder.imageView.setOnClickListener { onItemSelected(oneMovieEntity) }
        } else {
            wantMore(position..(position + 10))
        }
    }

    override fun onViewRecycled(holder: EntryViewHolder) {
        super.onViewRecycled(holder)
        renderer.cancelRequest(holder.imageView)
    }

    class EntryViewHolder(view: ImageView) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view
    }
}