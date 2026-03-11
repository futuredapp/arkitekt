package app.futured.arkitekt.crusecases.utils

/**
 * Returns a root cause of this exception or null if there is no
 */
val Throwable.rootCause: Throwable?
    get() {
        var currentCause: Throwable? = cause
        while (currentCause?.cause != null) {
            currentCause = currentCause.cause
        }
        return currentCause
    }
