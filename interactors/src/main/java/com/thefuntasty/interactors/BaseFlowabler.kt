package com.thefuntasty.interactors

import io.reactivex.Flowable

abstract class BaseFlowabler<T : Any> : BaseInteractor<T>() {

    abstract fun create(): Flowable<T>

    internal fun Flowable<T>.applySchedulers(): Flowable<T> {
        return compose { resultObservable ->
            resultObservable
                .subscribeOn(getWorkScheduler())
                .observeOn(getResultScheduler())
        }
    }
}
