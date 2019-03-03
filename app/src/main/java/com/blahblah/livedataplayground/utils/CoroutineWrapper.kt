package com.blahblah.livedataplayground.utils

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
    private val scope = CoroutineScope(ctx)

    fun launchUI(f: suspend CoroutineScope.() -> Unit) = scope.launch(Dispatchers.Main, block = f)
    fun launch(f: suspend CoroutineScope.() -> Unit) = scope.launch(Dispatchers.Default, block = f)
    suspend fun <T> withContext(f: suspend CoroutineScope.() -> T) =
        kotlinx.coroutines.withContext(ctx, block = f)
}