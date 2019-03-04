package com.blahblah.livedataplayground.fragments.adapters

import android.util.LruCache
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.blahblah.livedataplayground.R
import com.blahblah.livedataplayground.model.OneMovieEntity
import com.blahblah.livedataplayground.viewmodel.MoviesViewModel
import com.squareup.picasso.Picasso

/**
 * Description:
 * Created by shmuel on 2.3.19.
 */
class GalleryFragmentAdapter(
    private val lifeCycle: Lifecycle,
    viewModel: MoviesViewModel,
    private val wantMore: (IntRange) -> Unit,
    private val onItemSelected: (OneMovieEntity) -> Unit
) :
    RecyclerView.Adapter<GalleryFragmentAdapter.EntryViewHolder>() {
    private val cache = LruCache<Int, OneMovieEntity>(100)
    private var dataSize = 0
    private var renderer = Picasso.get()

    init {
        viewModel.moviesListPage.observe({ lifeCycle }, { chunk ->
            chunk.data.forEachIndexed { i, entity -> cache.put(i + chunk.firstPosition, entity) }
            dataSize = Math.max(dataSize, chunk.firstPosition + chunk.data.size)
            notifyDataSetChanged()
        })
        wantMore.invoke(0..40)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gallery_item, parent, false) as ImageView
        return EntryViewHolder(view)
    }

    // Request one more to make sure the next page is requested
    override fun getItemCount() = dataSize
    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        if (holder.imageView.tag != null && holder.imageView.tag != position) {
            renderer.cancelRequest(holder.imageView)
        }

        val oneMovieEntity = cache[position]
        if (oneMovieEntity != null) {
            val url: String = oneMovieEntity.posterUri
            holder.imageView.tag = position
            renderer
                .load(url)
                .placeholder(R.drawable.ic_movie)
                .error(R.drawable.ic_portable_wifi_off)
                .into(holder.imageView)
            (position..(position + 10)).find { cache[it] == null }?.let { pos -> wantMore(pos..(pos + 10)) }
            holder.imageView.setOnClickListener {
                onItemSelected(oneMovieEntity)
            }
        } else {
            wantMore(position..(position + 10))
        }
    }

    class EntryViewHolder(view: ImageView) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view
    }
}