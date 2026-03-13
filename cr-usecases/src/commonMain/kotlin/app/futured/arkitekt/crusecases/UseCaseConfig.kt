package app.futured.arkitekt.crusecases

/**
 * Holds references to lambdas and some basic configuration
 * used to process results of Coroutine use case.
 * Use [UseCaseConfig.Builder] to construct this object.
 */
class UseCaseConfig<T> private constructor(
    val onStart: () -> Unit,
    val onSuccess: (T) -> Unit,
    val onError: (Throwable) -> Unit,
    val disposePrevious: Boolean,
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
         * @param onStart Lambda called right before Coroutine is created
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

        fun build(): UseCaseConfig<T> = UseCaseConfig(
            onStart ?: { },
            onSuccess ?: { },
            onError ?: { throw it },
            disposePrevious,
        )
    }
}
