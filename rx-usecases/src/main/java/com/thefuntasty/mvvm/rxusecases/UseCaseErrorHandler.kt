package com.thefuntasty.mvvm.rxusecases

import app.futured.arkitekt.core.error.UseCaseErrorHandler

internal fun wrapWithGlobalOnErrorLogger(onError: (Throwable) -> Unit): (Throwable) -> Unit {
    return { error ->
        UseCaseErrorHandler.globalOnErrorLogger(error)
        onError(error)
    }
}
