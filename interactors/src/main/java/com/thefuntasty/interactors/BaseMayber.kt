package com.thefuntasty.interactors

import io.reactivex.Maybe

abstract class BaseMayber<T : Any> : BaseInteractor<T>() {

    protected abstract fun prepare(): Maybe<T>

    fun stream(): Maybe<T> = prepare().applySchedulers()

    private fun Maybe<T>.applySchedulers(): Maybe<T> {
        return compose { resultObservable ->
            resultObservable
                .subscribeOn(workScheduler)
                .observeOn(resultScheduler)
        }
    }
}
