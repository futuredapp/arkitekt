package com.thefuntasty.interactors.interactors

import io.reactivex.Observable

abstract class BaseObservabler<ARGS, T> : BaseInteractor() {

    protected abstract fun prepare(args: ARGS): Observable<T>

    fun create(args: ARGS): Observable<T> {
        return prepare(args).applySchedulers()
    }

    private fun Observable<T>.applySchedulers(): Observable<T> {
        return compose { resultObservable ->
            resultObservable
                .subscribeOn(workScheduler)
                .observeOn(resultScheduler)
        }
    }
}