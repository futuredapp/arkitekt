package app.futured.arkitekt.crusecases

import app.futured.arkitekt.crusecases.error.UseCaseErrorHandler
import app.futured.arkitekt.crusecases.utils.rootCause
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * This interface gives your class ability to execute [UseCase] and [FlowUseCase] Coroutine use cases.
 * You may find handy to implement this interface in custom Presenters, ViewHolders etc.
 * It is your responsibility to cancel [useCaseScope] when all running tasks should be stopped.
 */
interface CoroutineScopeOwner {
    /**
     * [CoroutineScope] scope used to execute coroutine based use cases. It is your responsibility to cancel it when all running
     * tasks should be stopped
     */
    val useCaseScope: CoroutineScope

    /**
     * Map of [Job] objects used to hold and cancel existing run of any [FlowUseCase] instance.
     */
    val useCaseJobPool: MutableMap<Any, Job>

    /**
     * Provides Dispatcher for background tasks. This may be overridden for testing purposes
     */
    fun getWorkerDispatcher() = Dispatchers.IO

    /**
     * Launch suspend [block] in [useCaseScope].
     *
     * Encapsulates this call with try catch block and when an exception is thrown
     * then it is logged in [UseCaseErrorHandler.globalOnErrorLogger] and handled by [defaultErrorHandler].
     *
     * If exception is [CancellationException] then [defaultErrorHandler] is not called and
     * [UseCaseErrorHandler.globalOnErrorLogger] is called only if the root cause of this exception is not
     * [CancellationException] (e.g. when [Result.getOrCancel] is used).
     */
    fun launchWithHandler(block: suspend CoroutineScope.() -> Unit) {
        useCaseScope.launch {
            try {
                block()
            } catch (exception: CancellationException) {
                val rootCause = exception.rootCause
                if (rootCause != null && rootCause !is CancellationException) {
                    UseCaseErrorHandler.globalOnErrorLogger(exception)
                }
            } catch (exception: Throwable) {
                UseCaseErrorHandler.globalOnErrorLogger(exception)
                defaultErrorHandler(exception)
            }
        }
    }

    /**
     * This method is called when coroutine launched with [launchWithHandler] throws an exception and
     * this exception isn't [CancellationException]. By default, it rethrows this exception.
     */
    fun defaultErrorHandler(exception: Throwable): Unit = throw exception
}
