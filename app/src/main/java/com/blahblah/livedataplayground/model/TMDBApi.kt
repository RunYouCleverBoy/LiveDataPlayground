package com.blahblah.livedataplayground.model

import android.content.Context
import android.content.pm.PackageManager
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject

/**
 * Description:
 * Created by shmuel on 28.2.19.
 */
class TMDBApi(context: Context) {
    private val metadata =
        context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA).metaData
    private val apiKey = metadata.getString("${context.packageName}.movieDbKey")
    private val baseUrl = "https://api.themoviedb.org/"
    private val client = OkHttpClient()
    private fun query(page: Int) = "${baseUrl}3/discover/movie?page=$page&api_key=$apiKey"

    fun getList(page: Int = 1): List<OneMovieEntity> {
        val request = Request.Builder().url(query(page)).build()
        val response = client.newCall(request).execute()
        val thumbnailsBaseUrl = "https://image.tmdb.org/t/p/w185"
        return try {
            val pageJson = JSONObject(response.body()?.string())
            val moviesJson = pageJson.getJSONArray("results")
            IntRange(0, moviesJson.length() - 1).map { i ->
                OneMovieEntity().apply {
                    val movieJson = moviesJson.optJSONObject(i)
                    id = movieJson.optInt("id", 0)
                    backdropUri = thumbnailsBaseUrl + movieJson.optString("backdrop_path", "")
                    posterUri = thumbnailsBaseUrl + movieJson.optString("poster_path", "")
                    synopsis = movieJson.optString("overview", "N/A")
                    movieName = movieJson.optString("title", "N/A")
                    popularity = movieJson.optDouble("popularity", 0.0)
                    cameFromPage = page
                }
            }
        } catch (jsonException: JSONException) {
            listOf()
        }


    }
}

