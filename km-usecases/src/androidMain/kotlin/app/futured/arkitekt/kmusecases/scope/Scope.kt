package app.futured.arkitekt.kmusecases.scope

import app.futured.arkitekt.kmusecases.usecase.FlowUseCase
import app.futured.arkitekt.kmusecases.usecase.UseCase
import app.futured.arkitekt.kmusecases.workerDispatcher
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

public actual interface Scope {
    public actual val coroutineScope: CoroutineScope

    public fun <Arg, ReturnType> UseCase<Arg, ReturnType>.execute(
        arg: Arg,
        config: UseCaseConfig.Builder<ReturnType>.() -> Unit
    ) {
        val useCaseConfig = UseCaseConfig.Builder<ReturnType>().run {
            config(this)
            return@run build()
        }
        if (useCaseConfig.disposePrevious) {
            job.get()?.cancel()
        }
        useCaseConfig.onStart()
        job.set(runJob(arg, this, useCaseConfig.onSuccess, useCaseConfig.onError))
    }

    private fun <Arg, ReturnType> runJob(
        arg: Arg,
        uc: UseCase<Arg, ReturnType>,
        onSuccess: (ReturnType) -> Unit,
        onError: (Throwable) -> Unit
    ): Job {
        return coroutineScope.async { uc.build(arg) }
            .also {
                coroutineScope.launch {
                    kotlin.runCatching { it.await() }
                        .fold(onSuccess, onError)
                }
            }
    }

    /**
     * Asynchronously executes use case and consumes data from flow on UI thread.
     * By default all previous pending executions are canceled, this can be changed
     * by [config]. When suspend function in use case finishes, onComplete is called
     * on UI thread. This version is gets initial arguments by [args].
     *
     * @param args Arguments used for initial use case initialization.
     * @param config [FlowUseCaseConfig] used to process results of internal
     * Flow and to set configuration options.
     **/
    fun <ARGS, ReturnType : Any?> FlowUseCase<ARGS, ReturnType>.execute(
        args: ARGS,
        config: FlowUseCaseConfig.Builder<ReturnType>.() -> Unit
    ) {
        val flowUseCaseConfig = FlowUseCaseConfig.Builder<ReturnType>().run {
            config.invoke(this)
            return@run build()
        }

        if (flowUseCaseConfig.disposePrevious) {
            job.get()?.cancel()
        }

        job.set(build(args)
            .flowOn(workerDispatcher)
            .onStart { flowUseCaseConfig.onStart() }
            .onEach { flowUseCaseConfig.onNext(it) }
            .onCompletion { error ->
                when {
                    error is CancellationException -> {
                        // ignore this exception
                    }
                    error != null -> flowUseCaseConfig.onError(error)
                    else -> flowUseCaseConfig.onComplete()
                }
            }
            .catch { /* handled in onCompletion */ }
            .launchIn(coroutineScope))
    }

    /**
     * Holds references to lambdas and some basic configuration
     * used to process results of Coroutine use case.
     * Use [UseCaseConfig.Builder] to construct this object.
     */
    class UseCaseConfig<T> private constructor(
        val onStart: () -> Unit,
        val onSuccess: (T) -> Unit,
        val onError: (Throwable) -> Unit,
        val disposePrevious: Boolean
    ) {
        /**
         * Constructs references to lambdas and some basic configuration
         * used to process results of Coroutine use case.
         */
        class Builder<T> {
            private var onStart: (() -> Unit)? = null
            private var onSuccess: ((T) -> Unit)? = null
            private var onError: ((Throwable) -> Unit)? = null
            private var disposePrevious = true

            /**
             * Set lambda that is called right before
             * the internal Coroutine is created
             * @param onStart Lambda called right before Coroutine is
             * created
             */
            fun onStart(onStart: () -> Unit) {
                this.onStart = onStart
            }

            /**
             * Set lambda that is called when internal Coroutine
             * finished without exceptions
             * @param onSuccess Lambda called when Coroutine finished
             */
            fun onSuccess(onSuccess: (T) -> Unit) {
                this.onSuccess = onSuccess
            }

            /**
             * Set lambda that is called when exception on
             * internal Coroutine occurs
             * @param onError Lambda called when exception occurs
             */
            fun onError(onError: (Throwable) -> Unit) {
                this.onError = onError
            }

            /**
             * Set whether currently active Job of internal Coroutine
             * should be canceled when execute is called repeatedly.
             * Default value is true.
             * @param disposePrevious True if active Job of internal
             * Coroutine should be canceled. Default value is true.
             */
            fun disposePrevious(disposePrevious: Boolean) {
                this.disposePrevious = disposePrevious
            }

            fun build(): UseCaseConfig<T> {
                return UseCaseConfig(
                    onStart ?: { },
                    onSuccess ?: { },
                    onError ?: { throw it },
                    disposePrevious
                )
            }
        }
    }

    /**
     * Holds references to lambdas and some basic configuration
     * used to process results of Flow use case.
     * Use [FlowUseCaseConfig.Builder] to construct this object.
     */
    class FlowUseCaseConfig<T> private constructor(
        val onStart: () -> Unit,
        val onNext: (T) -> Unit,
        val onError: (Throwable) -> Unit,
        val onComplete: () -> Unit,
        val disposePrevious: Boolean
    ) {
        /**
         * Constructs references to lambdas and some basic configuration
         * used to process results of Flow use case.
         */
        class Builder<T> {
            private var onStart: (() -> Unit)? = null
            private var onNext: ((T) -> Unit)? = null
            private var onError: ((Throwable) -> Unit)? = null
            private var onComplete: (() -> Unit)? = null
            private var disposePrevious = true

            /**
             * Set lambda that is called right before
             * internal Job of Flow is launched.
             * @param onStart Lambda called right before Flow Job is launched.
             */
            fun onStart(onStart: () -> Unit) {
                this.onStart = onStart
            }

            /**
             * Set lambda that is called when internal Flow emits new value
             * @param onNext Lambda called for every new emitted value
             */
            fun onNext(onNext: (T) -> Unit) {
                this.onNext = onNext
            }

            /**
             * Set lambda that is called when some exception on
             * internal Flow occurs
             * @param onError Lambda called when exception occurs
             */
            fun onError(onError: (Throwable) -> Unit) {
                this.onError = onError
            }

            /**
             * Set lambda that is called when internal Flow is completed
             * without errors
             * @param onComplete Lambda called when Flow is completed
             * without errors
             */
            fun onComplete(onComplete: () -> Unit) {
                this.onComplete = onComplete
            }

            /**
             * Set whether currently running Job of internal Flow
             * should be canceled when execute is called repeatedly.
             * @param disposePrevious True if currently running
             * Job of internal Flow should be canceled
             */
            fun disposePrevious(disposePrevious: Boolean) {
                this.disposePrevious = disposePrevious
            }

            fun build(): FlowUseCaseConfig<T> {
                return FlowUseCaseConfig(
                    onStart ?: { },
                    onNext ?: { },
                    onError ?: { throw it },
                    onComplete ?: { },
                    disposePrevious
                )
            }
        }
    }
}
