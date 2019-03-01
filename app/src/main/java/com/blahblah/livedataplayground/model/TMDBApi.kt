package com.blahblah.livedataplayground.model

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri

/**
 * Description:
 * Created by shmuel on 28.2.19.
 */
class TMDBApi(context: Context) {
    private val metadata =
        context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA).metaData
    private val apiKey = metadata.getString("${context.packageName}.movieDbKey")

    private fun templateToUri(template: String) = Uri.parse(template.replace("{API_KEY}", apiKey))

    companion object {
        private const val tmdbAuthTemplate = "https://api.themoviedb.org/3/authentication/token/new?api_key={API_KEY}"
    }
}
