package com.thefuntasty.interactors.interactors

import io.reactivex.Flowable

abstract class BaseFlowabler<ARGS, T> : BaseInteractor() {

    protected abstract fun prepare(args: ARGS): Flowable<T>

    fun create(args: ARGS): Flowable<T> {
        return prepare(args).applySchedulers()
    }

    private fun Flowable<T>.applySchedulers(): Flowable<T> {
        return compose { resultObservable ->
            resultObservable
                .subscribeOn(workScheduler)
                .observeOn(resultScheduler)
        }
    }
}