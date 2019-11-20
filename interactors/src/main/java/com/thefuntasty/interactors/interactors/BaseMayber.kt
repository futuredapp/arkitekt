package com.thefuntasty.interactors.interactors

import io.reactivex.Maybe

abstract class BaseMayber<ARGS, T> : BaseInteractor() {

    protected abstract fun prepare(args: ARGS): Maybe<T>

    fun create(args: ARGS): Maybe<T> = prepare(args).applySchedulers()

    private fun Maybe<T>.applySchedulers(): Maybe<T> {
        return compose { resultObservable ->
            resultObservable
                .subscribeOn(workScheduler)
                .observeOn(resultScheduler)
        }
    }
}
