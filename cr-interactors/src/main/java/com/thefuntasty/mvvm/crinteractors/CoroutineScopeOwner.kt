package com.thefuntasty.mvvm.crinteractors

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * This interface gives your class ability to execute [BaseCoroutineInteractor] and [BaseFlowInteractor] interactors
 * You may find handy to implement this interface in custom Presenters, ViewHolders etc.
 * It is your responsibility to cancel [coroutineScope] when when all running tasks should be stopped.
 */
interface CoroutineScopeOwner {

    val coroutineScope: CoroutineScope

    fun <T : Any?> BaseCoroutineInteractor<T>.execute(onSuccess: (T) -> Unit) = execute(
        onSuccess,
        null
    )

    /**
     * Asynchronously executes interactor, all previous pending executions are canceled
     */
    fun <T : Any?> BaseCoroutineInteractor<T>.execute(
        onSuccess: (T) -> Unit,
        onError: ((Throwable) -> Unit)?
    ) {
        deferred?.cancel()
        deferred = coroutineScope.async(kotlinx.coroutines.Dispatchers.IO) {
            build()
        }.also {
            coroutineScope.launch(kotlinx.coroutines.Dispatchers.Main) {
                try {
                    onSuccess(it.await())
                } catch (error: Throwable) {
                    onError?.invoke(error) ?: throw error
                }
            }
        }
    }

    /**
     * Asynchronously executes interactor and consumes data from flow on UI thread,
     * all previous pending executions are canceled.
     * When suspend function in interactor ends onComplete is called on UI thread
     **/
    fun <T : Any?> BaseFlowInteractor<T>.execute(
        onNext: (T) -> Unit = {},
        onError: ((Throwable) -> Unit)? = null,
        onComplete: () -> Unit = {}
    ) {
        job?.cancel()
        job = coroutineScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            build()
                .onEach {
                    kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                        onNext(it)
                    }
                }
                .onCompletion { error ->
                    kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                        error?.also { onError?.invoke(it) ?: throw error } ?: onComplete()
                    }
                }
                .collect()
        }
    }
}
