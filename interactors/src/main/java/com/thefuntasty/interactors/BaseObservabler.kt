package com.thefuntasty.interactors

import io.reactivex.Observable

abstract class BaseObservabler<T : Any> : BaseInteractor<T>() {

    protected abstract fun prepare(): Observable<T>

    open fun stream(): Observable<T> = prepare().applySchedulers()

    private fun Observable<T>.applySchedulers(): Observable<T> {
        return compose { resultObservable ->
            resultObservable
                .subscribeOn(getWorkScheduler())
                .observeOn(getResultScheduler())
        }
    }
}
