package com.blahblah.livedataplayground.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Description: Wrapper for coroutines
 * Created by shmuel on 28.2.19.
 */
class CoroutineWrapper(private val scope: CoroutineScope = GlobalScope) {
    fun launchUI(f: suspend CoroutineScope.() -> Unit) = scope.launch(Dispatchers.Main, block = f)
    fun launch(f: suspend CoroutineScope.() -> Unit) = scope.launch(Dispatchers.Default, block = f)
    suspend fun withContext(f: suspend CoroutineScope.() -> Unit) =
        kotlinx.coroutines.withContext(scope.coroutineContext, block = f)
}