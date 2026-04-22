package app.futured.arkitekt.crusecases

import app.futured.arkitekt.crusecases.error.UseCaseErrorHandler
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * Asynchronously executes use case and saves it's Deferred. By default, all previous
 * pending executions are canceled, this can be changed by the [config].
 * This version is used for use cases without initial arguments.
 *
 * @param config [UseCaseConfig] used to process results of internal
 * Coroutine and to set configuration options.
 */
context(coroutineScopeOwner: CoroutineScopeOwner)
fun <T : Any?> UseCase<Unit, T>.execute(config: UseCaseConfig.Builder<T>.() -> Unit) =
    execute(Unit, config)

/**
 * Asynchronously executes use case and saves it's Deferred. By default, all previous
 * pending executions are canceled, this can be changed by the [config].
 * This version gets initial arguments by [args].
 *
 * In case that an error is thrown during the execution of [UseCase] then [UseCaseErrorHandler.globalOnErrorLogger]
 * is called with the error as an argument.
 *
 * @param args Arguments used for initial use case initialization.
 * @param config [UseCaseConfig] used to process results of internal
 * Coroutine and to set configuration options.
 */
context(coroutineScopeOwner: CoroutineScopeOwner)
fun <ARGS, T : Any?> UseCase<ARGS, T>.execute(
    args: ARGS,
    config: UseCaseConfig.Builder<T>.() -> Unit,
) {
    internalExecute(args, coroutineScopeOwner, config)
}

/**
 * Synchronously executes use case and saves it's Deferred. By default all previous
 * pending executions are canceled, this can be changed by the [cancelPrevious].
 * This version is used for use cases without initial arguments.
 *
 * @return [Result] that encapsulates either a successful result with [Success] or a failed result with [Error]
 */
context(coroutineScopeOwner: CoroutineScopeOwner)
suspend fun <T : Any?> UseCase<Unit, T>.execute(cancelPrevious: Boolean = true) = execute(Unit, cancelPrevious)

/**
 * Synchronously executes use case and saves it's Deferred. By default all previous
 * pending executions are canceled, this can be changed by the [cancelPrevious].
 * This version gets initial arguments by [args].
 *
 * [UseCaseErrorHandler.globalOnErrorLogger] is not used in this version of the execute method since it is
 * recommended to call all execute methods with [Result] return type from [launchWithHandler] method where
 * [UseCaseErrorHandler.globalOnErrorLogger] is used.
 *
 * @param args Arguments used for initial use case initialization.
 * @return [Result] that encapsulates either a successful result with [Success] or a failed result with [Error]
 */
@Suppress("TooGenericExceptionCaught")
context(coroutineScopeOwner: CoroutineScopeOwner)
suspend fun <ARGS, T : Any?> UseCase<ARGS, T>.execute(args: ARGS, cancelPrevious: Boolean = true): Result<T> {
    if (cancelPrevious) {
        deferred?.cancel()
    }
    return try {
        val newDeferred = coroutineScopeOwner.coroutineScope
            .async(coroutineScopeOwner.getWorkerDispatcher(), CoroutineStart.LAZY) {
                build(args)
            }.also { deferred = it }
        Success(newDeferred.await())
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Throwable) {
        Error(exception)
    }
}

@Deprecated(
    message = "Use the version with CoroutineScopeOwner as context parameter instead. Enable context parameters by adding '-Xcontext-receivers' compiler flag.",
    replaceWith = ReplaceWith("execute(args, config)", imports = ["app.futured.arkitekt.crusecases.execute"])
)
fun <ARGS, T : Any?> UseCase<ARGS, T>.execute(
    args: ARGS,
    coroutineScopeOwner: CoroutineScopeOwner,
    config: UseCaseConfig.Builder<T>.() -> Unit,
) {
    internalExecute(args, coroutineScopeOwner, config)
}

@Deprecated(
    message = "Use the version with CoroutineScopeOwner as context parameter instead. Enable context parameters by adding '-Xcontext-receivers' compiler flag.",
    replaceWith = ReplaceWith("execute(args, config)", imports = ["app.futured.arkitekt.crusecases.execute"])
)
fun <T : Any?> UseCase<Unit, T>.execute(
    coroutineScopeOwner: CoroutineScopeOwner,
    config: UseCaseConfig.Builder<T>.() -> Unit,
) {
    internalExecute(Unit, coroutineScopeOwner, config)
}

private fun <ARGS, T : Any?> UseCase<ARGS, T>.internalExecute(
    args: ARGS,
    coroutineScopeOwner: CoroutineScopeOwner,
    config: UseCaseConfig.Builder<T>.() -> Unit,
) {
    val useCaseConfig = UseCaseConfig.Builder<T>().run {
        config.invoke(this)
        return@run build()
    }
    if (useCaseConfig.disposePrevious) {
        deferred?.cancel()
    }

    useCaseConfig.onStart()
    deferred = coroutineScopeOwner.coroutineScope
        .async(context = coroutineScopeOwner.getWorkerDispatcher(), start = CoroutineStart.LAZY) {
            build(args)
        }
        .also {
            coroutineScopeOwner.coroutineScope.launch(Dispatchers.Main) {
                try {
                    useCaseConfig.onSuccess(it.await())
                } catch (cancellation: CancellationException) {
                    // do nothing - this is normal way of suspend function interruption
                } catch (error: Throwable) {
                    UseCaseErrorHandler.globalOnErrorLogger(error)
                    useCaseConfig.onError(error)
                }
            }
        }
}
