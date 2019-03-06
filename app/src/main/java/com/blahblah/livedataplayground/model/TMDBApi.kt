package com.blahblah.livedataplayground.model

import android.content.Context
import com.blahblah.livedataplayground.R
import com.blahblah.livedataplayground.utils.addToIfNotEmpty
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

/**
 * Description:
 * Created by shmuel on 28.2.19.
 */
class TMDBApi(context: Context) {
    private val apiKey = context.getString(R.string.tmbdKey)
    private val baseUrl = "https://api.themoviedb.org/"
    private val client = OkHttpClient()
    private fun query(page: Int) = "${baseUrl}3/discover/movie?page=$page&api_key=$apiKey"

    fun getList(page: Int = 1): List<OneMovieEntity> {
        val request = Request.Builder().url(query(page)).build()
        val response = try {
            client.newCall(request).execute()
        } catch (io: IOException) {
            return listOf()
        }
        val thumbnailsBaseUrl = "https://image.tmdb.org/t/p/w185"
        val backdropBaseUrl = "https://image.tmdb.org/t/p/w500"
        return try {
            val pageJson = JSONObject(response.body()?.string())
            val moviesJson = pageJson.getJSONArray("results")
            IntRange(0, moviesJson.length() - 1).map { i ->
                OneMovieEntity().apply {
                    val movieJson = moviesJson.optJSONObject(i)
                    id = movieJson.optInt("id", 0)
                    backdropUri = movieJson.optString("backdrop_path", "").addToIfNotEmpty(thumbnailsBaseUrl)
                    posterUri = movieJson.optString("poster_path", "").addToIfNotEmpty(backdropBaseUrl)
                    synopsis = movieJson.optString("overview", "N/A")
                    movieName = movieJson.optString("title", "N/A")
                    popularity = movieJson.optDouble("popularity", 0.0)
                    cameFromPage = page
                }
            }
        } catch (exception: JSONException) {
            listOf()
        }
    }
}