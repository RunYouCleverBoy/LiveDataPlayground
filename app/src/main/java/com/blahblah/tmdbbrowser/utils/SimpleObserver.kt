package com.blahblah.tmdbbrowser.utils

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

/**
 * Description: Simple callback service
 *
 * As opposed to an Observable, the following callback service is based on a single param
 *  and manages the observers by handle. It is also typed.
 *
 * Created by shmuel on 6.3.19.
 * @param DT - Data type
 */
class SimpleObserver<DT> {
    private val handle = AtomicInteger(0)
    private val repository = ConcurrentHashMap<Int, (DT) -> Unit>()

    /**
     * Register an observer callback. Keep the handle for [removeObserver]
     *
     * @param callback observer callback
     *
     * @return handle for observer maintenance
     */
    fun addObserver(callback: (DT) -> Unit) = handle.incrementAndGet().also { repository[it] = callback }

    /**
     * Unregister an observer callback from [addObserver]
     *
     * @param handle received by [addObserver] at registration.
     * If the handle is not valid or <=0 it does nothing.
     *
     * @return True iff some observer was actually removed
     */
    fun removeObserver(handle: Int) = repository.remove(handle)

    /**
     * Notify all observers with a new datum
     *
     * @param data item to be distributed among observers
     */
    fun notifyObservers(data: DT) {
        repository.values.forEach { it.invoke(data) }
    }
}