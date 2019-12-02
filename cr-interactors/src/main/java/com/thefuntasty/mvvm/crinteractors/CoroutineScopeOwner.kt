package com.thefuntasty.mvvm.crinteractors

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * This interface gives your class ability to execute [BaseUseCase] and [BaseFlowUseCase] Coroutine use cases.
 * You may find handy to implement this interface in custom Presenters, ViewHolders etc.
 * It is your responsibility to cancel [coroutineScope] when when all running tasks should be stopped.
 */
interface CoroutineScopeOwner {

    /**
     * [CoroutineScope] scope used to execute coroutine based use cases. It is your responsibility to cancel it when all running
     * tasks should be stopped
     */
    val coroutineScope: CoroutineScope

    /**
     * Provides Dispatcher for background tasks. This may be overridden for testing purposes
     */
    fun getWorkerDispatcher() = Dispatchers.IO

    /**
     * Asynchronously executes use case and saves it's Deferred. By default all previous
     * pending executions are canceled, which can be changed by the [config].
     * This version is used for use cases without initial arguments.
     *
     * @param config [UseCaseConfig] used to process results of internal
     * Coroutine and to set configuration options.
     */
    fun <T : Any?> BaseUseCase<Unit, T>.execute(config: UseCaseConfig.Builder<T>.() -> Unit) =
        execute(Unit, config)

    /**
     * Asynchronously executes use case and saves it's Deferred. By default all previous
     * pending executions are canceled, which can be changed by the [config].
     * This version gets initial arguments by [args].
     *
     * @param args Arguments used for initial use case initialization.
     * @param config [UseCaseConfig] used to process results of internal
     * Coroutine and to set configuration options.
     */
    fun <ARGS, T : Any?> BaseUseCase<ARGS, T>.execute(
        args: ARGS,
        config: UseCaseConfig.Builder<T>.() -> Unit
    ) {
        val useCaseConfig = UseCaseConfig.Builder<T>().run {
            config.invoke(this)
            return@run build()
        }
        try {
            if (useCaseConfig.disposePrevious) {
                deferred?.cancel()
            }

            useCaseConfig.onStart()
            deferred = coroutineScope.async(getWorkerDispatcher()) {
                build(args)
            }.also {
                coroutineScope.launch(Dispatchers.Main) {
                    try {
                        useCaseConfig.onSuccess(it.await())
                    } catch (error: Throwable) {
                        useCaseConfig.onError.invoke(error)
                    }
                }
            }
        } catch (cancellation: CancellationException) {
            // do nothing - this is normal way of suspend function interruption
        } catch (error: Throwable) {
            useCaseConfig.onError.invoke(error)
        }
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
             * Set lambda which is called right before
             * the internal Coroutine is created
             * @param onStart Lambda called right before Coroutine is
             * created
             */
            fun onStart(onStart: () -> Unit) {
                this.onStart = onStart
            }

            /**
             * Set lambda which is called when internal Coroutine
             * finished without exceptions
             * @param onSuccess Lambda called when Coroutine finished
             */
            fun onSuccess(onSuccess: (T) -> Unit) {
                this.onSuccess = onSuccess
            }

            /**
             * Set lambda which is called when exception on
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

    fun <T : Any?> BaseFlowUseCase<Unit, T>.execute(config: FlowUseCaseConfig.Builder<T>.() -> Unit) =
        execute(Unit, config)

    /**
     * Asynchronously executes use case and consumes data from flow on UI thread.
     * By default all previous pending executions are canceled, which can be changed
     * by [config]. When suspend function in use case finishes, onComplete is called
     * on UI thread. This version is gets initial arguments by [args].
     *
     * @param args Arguments used for initial use case initialization.
     * @param config [FlowUseCaseConfig] used to process results of internal
     * Flow and to set configuration options.
     **/
    fun <ARGS, T : Any?> BaseFlowUseCase<ARGS, T>.execute(
        args: ARGS,
        config: FlowUseCaseConfig.Builder<T>.() -> Unit
    ) {
        val flowUsecaseConfig = FlowUseCaseConfig.Builder<T>().run {
            config.invoke(this)
            return@run build()
        }

        if (flowUsecaseConfig.disposePrevious) {
            job?.cancel()
        }

        flowUsecaseConfig.onStart()
        job = coroutineScope.launch(getWorkerDispatcher()) {
            try {
                build(args)
                    .onEach {
                        kotlinx.coroutines.withContext(Dispatchers.Main) {
                            flowUsecaseConfig.onNext(it)
                        }
                    }
                    .onCompletion { error ->
                        kotlinx.coroutines.withContext(Dispatchers.Main) {
                            error?.also { flowUsecaseConfig.onError.invoke(it) } ?: flowUsecaseConfig.onComplete()
                        }
                    }
                    .collect()
            } catch (cancellation: CancellationException) {
                // do nothing - this is normal way of suspend function interruption
            } catch (error: Throwable) {
                flowUsecaseConfig.onError.invoke(error)
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
             * Set lambda which is called right before
             * internal Job of Flow is launched.
             * @param onStart Lambda called right before Flow Job is launched.
             */
            fun onStart(onStart: () -> Unit) {
                this.onStart = onStart
            }

            /**
             * Set lambda which is called when internal Flow emits new value
             * @param onNext Lambda called for every new emitted value
             */
            fun onNext(onNext: (T) -> Unit) {
                this.onNext = onNext
            }

            /**
             * Set lambda which is called when some exception on
             * internal Flow occurs
             * @param onError Lambda called when exception occurs
             */
            fun onError(onError: (Throwable) -> Unit) {
                this.onError = onError
            }

            /**
             * Set lambda which is called when internal Flow is completed
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
