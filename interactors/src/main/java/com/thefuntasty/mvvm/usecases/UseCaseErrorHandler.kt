package com.thefuntasty.mvvm.usecases

/**
 * This object is used for global handling and logging of errors that are thrown in the interactor execution.
 */
object UseCaseErrorHandler {

    /**
     * Lambda expression that is invoked every time when an error
     * is propagated to the subscriber. It should be used for
     * the global logging of error events of interactors.
     */
    var globalOnErrorLogger: (Throwable) -> Unit = {}
}

internal fun wrapWithGlobalOnErrorLogger(onError: (Throwable) -> Unit): (Throwable) -> Unit {
    return { error ->
        UseCaseErrorHandler.globalOnErrorLogger(error)
        onError(error)
    }
}
