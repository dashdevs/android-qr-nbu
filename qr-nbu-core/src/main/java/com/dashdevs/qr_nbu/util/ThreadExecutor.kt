package com.dashdevs.qr_nbu.util

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * A class to be used for performing asynchronous operations
 */
internal class ThreadExecutor {
    /**
     * An Executor Service that executes work on background thread
     */
    private val ioExecutor
        by lazy { Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2) }

    /**
     * An Executor that executes work on main application thread
     */
    private val mainExecutor: MainThreadExecutor by lazy { MainThreadExecutor() }

    /**
     * This method is used to execute asynchronous operation[action] and pass the result of execution
     * to main thread operation[onResult]
     *
     * @param T A data type to be returned from [action] function
     * @param action A lambda expression that will be executed on background thread
     * @param onResult A lambda expression that will be executed on main application thread
     */
    fun <T> execute(action: () -> T, onResult: (T) -> Unit) {
        doIOAction {
            val result = action()
            mainExecutor.execute { onResult(result) }
        }
    }

    /**
     * This method is used to execute asynchronous operation[action]
     *
     * @param action A lambda expression that will be executed on background thread
     */
    private fun doIOAction(action: () -> Unit) = ioExecutor.execute(action)

    /**
     * A class to be used for executing operations on main application thread
     */
    private class MainThreadExecutor : Executor {
        private val mainHandler = Handler(Looper.getMainLooper())
        override fun execute(action: Runnable) {
            mainHandler.post(action)
        }
    }
}