package com.blahblah.tmdbbrowser.viewmodel

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

/**
 * Description: Simple callback service
 * Created by shmuel on 6.3.19.
 * @param DT - Data type
 */
class SimpleStrongListener<DT> {
    private val handle = AtomicInteger(0)
    private val repository = ConcurrentHashMap<Int, (DT) -> Unit>()
    fun register(callback: (DT) -> Unit) = handle.incrementAndGet().also { repository[it] = callback }
    fun unRegister(handle: Int) = repository.remove(handle)
    fun callWith(data: DT) {
        repository.values.forEach { it.invoke(data) }
    }
}