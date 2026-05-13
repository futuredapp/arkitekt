package app.futured.arkitekt.crusecases

import app.futured.arkitekt.crusecases.error.UseCaseErrorHandler
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

context(coroutineScopeOwner: CoroutineScopeOwner)
fun <T : Any?> FlowUseCase<Unit, T>.execute(
    config: FlowUseCaseConfig.Builder<T, T>.() -> Unit,
): Unit = execute(Unit, config)

/**
 * Asynchronously executes use case and consumes data from flow on UI thread.
 * By default all previous pending executions are canceled, this can be changed
 * by [config]. When suspend function in use case finishes, onComplete is called
 * on UI thread. This version gets initial arguments by [args].
 *
 * In case that an error is thrown during the execution of [FlowUseCase] then
 * [UseCaseErrorHandler.globalOnErrorLogger] is called with the error as an argument.
 *
 * @param args Arguments used for initial use case initialization.
 * @param config [FlowUseCaseConfig] used to process results of internal
 * Flow and to set configuration options.
 **/
context(coroutineScopeOwner: CoroutineScopeOwner)
fun <ARGS, T : Any?> FlowUseCase<ARGS, T>.execute(
    args: ARGS,
    config: FlowUseCaseConfig.Builder<T, T>.() -> Unit,
) {
    internalExecute(args, coroutineScopeOwner, config)
}

private fun <ARGS, T : Any?> FlowUseCase<ARGS, T>.internalExecute(
    args: ARGS,
    coroutineScopeOwner: CoroutineScopeOwner,
    config: FlowUseCaseConfig.Builder<T, T>.() -> Unit,
) {
    val flowUseCaseConfig =
        FlowUseCaseConfig.Builder<T, T>().run {
            config.invoke(this)
            return@run build()
        }

    if (flowUseCaseConfig.disposePrevious) {
        coroutineScopeOwner.useCaseJobPool[this]?.cancel()
    }

    coroutineScopeOwner.useCaseJobPool[this] =
        build(args)
            .flowOn(coroutineScopeOwner.getWorkerDispatcher())
            .onStart { flowUseCaseConfig.onStart() }
            .onEach { flowUseCaseConfig.onNext(it) }
            .onCompletion { error ->
                when {
                    error is CancellationException -> {
                        // ignore this exception
                    }

                    error != null -> {
                        UseCaseErrorHandler.globalOnErrorLogger(error)
                        flowUseCaseConfig.onError(error)
                    }

                    else -> {
                        flowUseCaseConfig.onComplete()
                    }
                }
            }.catch { /* handled in onCompletion */ }
            .launchIn(coroutineScopeOwner.useCaseScope)
}


@Deprecated(
    message = "Use the version with CoroutineScopeOwner as context parameter instead. Enable context parameters by adding '-Xcontext-receivers' compiler flag.",
    replaceWith = ReplaceWith("execute(args, config)", imports = ["app.futured.arkitekt.crusecases.execute"])
)
fun <ARGS, T : Any?> FlowUseCase<ARGS, T>.execute(
    args: ARGS,
    coroutineScopeOwner: CoroutineScopeOwner,
    config: FlowUseCaseConfig.Builder<T, T>.() -> Unit,
) {
    internalExecute(args, coroutineScopeOwner, config)
}

@Deprecated(
    message = "Use the version with CoroutineScopeOwner as context parameter instead. Enable context parameters by adding '-Xcontext-receivers' compiler flag.",
    replaceWith = ReplaceWith("execute(config)", imports = ["app.futured.arkitekt.crusecases.execute"])
)
fun <T : Any?> FlowUseCase<Unit, T>.execute(
    coroutineScopeOwner: CoroutineScopeOwner,
    config: FlowUseCaseConfig.Builder<T, T>.() -> Unit,
) {
    internalExecute(Unit, coroutineScopeOwner, config)
}
