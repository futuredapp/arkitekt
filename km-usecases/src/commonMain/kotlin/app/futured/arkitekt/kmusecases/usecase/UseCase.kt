package app.futured.arkitekt.kmusecases.usecase

import app.futured.arkitekt.kmusecases.atomics.AtomicRef
import app.futured.arkitekt.kmusecases.scope.Scope
import app.futured.arkitekt.kmusecases.freeze
import app.futured.arkitekt.kmusecases.workerDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class UseCase<Arg, ReturnType> {

    var job: AtomicRef<Job?> = AtomicRef(null)

//    init { todo this must be in child, not here
//        freeze()
//    }

    abstract suspend fun build(arg: Arg): ReturnType

    // because of iOS, not accessible from Android
    // on iOS will be compiled to UC.execute(scope: Scope, arg...
    fun Scope.execute(
        arg: Arg,
        config: UseCaseConfig.Builder<ReturnType>.() -> Unit
    ) {
        val useCaseConfig = UseCaseConfig.Builder<ReturnType>().run {
            config(this)
            return@run build()
        }.freeze()
        if (useCaseConfig.disposePrevious) {
            job.get()?.cancel()
        }
        useCaseConfig.onStart()
        job.set(getJob(arg, useCaseConfig.onSuccess, useCaseConfig.onError))
    }

    private fun Scope.getJob(
        arg: Arg,
        onSuccess: (ReturnType) -> Unit,
        onError: (Throwable) -> Unit
    ): Job {
        arg.freeze()
        onSuccess.freeze()
        onError.freeze()
        return coroutineScope.async { buildOnBg(arg) }
            .also {
                coroutineScope.launch {
                    kotlin.runCatching { it.await() }
                        .fold(onSuccess, onError)
                }
            }.freeze()
    }

//    private suspend fun buildOnBg(arg: Arg) = withContext(workerDispatcher) {
    private suspend fun buildOnBg(arg: Arg) = withContext(Dispatchers.Default) {
        this@UseCase.build(arg)
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

            internal fun build(): UseCaseConfig<T> {
                return UseCaseConfig(
                    onStart ?: { },
                    onSuccess ?: { },
                    onError ?: { throw it },
                    disposePrevious
                )
            }
        }
    }
}
