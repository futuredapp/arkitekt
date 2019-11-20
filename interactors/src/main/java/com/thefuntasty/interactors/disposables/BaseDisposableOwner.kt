package com.thefuntasty.interactors.disposables

import com.thefuntasty.interactors.InteractorErrorHandler

interface BaseDisposableOwner {

    fun wrapWithGlobalOnErrorLogger(onError: (Throwable) -> Unit): (Throwable) -> Unit {
        return { error ->
            InteractorErrorHandler.globalOnErrorLogger(error)
            onError(error)
        }
    }
}
