package com.thefuntasty.mvvm.rxusecases

import com.thefuntasty.mvvm.error.UseCaseErrorHandler

internal fun wrapWithGlobalOnErrorLogger(onError: (Throwable) -> Unit): (Throwable) -> Unit {
    return { error ->
        UseCaseErrorHandler.globalOnErrorLogger(error)
        onError(error)
    }
}
