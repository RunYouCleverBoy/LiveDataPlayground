package com.blahblah.tmdbbrowser.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Description: Wrapper for coroutines
 * Created by shmuel on 28.2.19.
 */
class CoroutineWrapper(private val ctx: CoroutineContext = GlobalScope.coroutineContext) {
    suspend fun <T> withContext(f: suspend CoroutineScope.() -> T) =
        kotlinx.coroutines.withContext(ctx, block = f)

    companion object {
        fun launchUI(f: suspend CoroutineScope.() -> Unit) = GlobalScope.launch(Dispatchers.Main, block = f)
    }
}