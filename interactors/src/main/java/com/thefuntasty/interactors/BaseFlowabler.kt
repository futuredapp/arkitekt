package com.thefuntasty.interactors

import io.reactivex.Flowable

abstract class BaseFlowabler<T : Any> : BaseInteractor<T>() {

    protected abstract fun prepare(): Flowable<T>

    open fun stream(): Flowable<T> = prepare().applySchedulers()

    private fun Flowable<T>.applySchedulers(): Flowable<T> {
        return compose { resultObservable ->
            resultObservable
                .subscribeOn(workScheduler)
                .observeOn(resultScheduler)
        }
    }
}
