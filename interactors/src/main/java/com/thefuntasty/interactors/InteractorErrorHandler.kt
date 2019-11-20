package com.thefuntasty.interactors

/**
 * This object is used for global handling and logging of errors that are thrown in the interactor execution.
 */
object InteractorErrorHandler {

    /**
     * Lambda expression that is invoked every time when an error
     * is propagated to the subscriber. It should be used for
     * the global logging of error events of interactors.
     */
    var globalOnErrorLogger: (Throwable) -> Unit = {}
}
