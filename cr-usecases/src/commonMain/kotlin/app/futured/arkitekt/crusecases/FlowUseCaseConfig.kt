package app.futured.arkitekt.crusecases

/**
 * Holds references to lambdas and some basic configuration
 * used to process results of Flow use case.
 * Use [FlowUseCaseConfig.Builder] to construct this object.
 */
class FlowUseCaseConfig<T, M> private constructor(
    val onStart: () -> Unit,
    val onNext: (M) -> Unit,
    val onError: (Throwable) -> Unit,
    val onComplete: () -> Unit,
    val disposePrevious: Boolean,
    val onMap: ((T) -> M)? = null,
) {
    /**
     * Constructs references to lambdas and some basic configuration
     * used to process results of Flow use case.
     */
    class Builder<T, M> {
        private var onStart: (() -> Unit)? = null
        private var onNext: ((M) -> Unit)? = null
        private var onMap: ((T) -> M)? = null
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
        fun onNext(onNext: (M) -> Unit) {
            this.onNext = onNext
        }

        /**
         * Set lambda that maps emitted values before [onNext] is called
         * @param onMap Lambda called for mapping every emitted value
         */
        fun onMap(onMap: (T) -> M) {
            this.onMap = onMap
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

        fun build(): FlowUseCaseConfig<T, M> =
            FlowUseCaseConfig(
                onStart ?: { },
                onNext ?: { },
                onError ?: { throw it },
                onComplete ?: { },
                disposePrevious,
                onMap,
            )
    }
}
