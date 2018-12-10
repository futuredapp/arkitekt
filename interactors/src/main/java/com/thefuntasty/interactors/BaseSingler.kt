package com.thefuntasty.interactors

import io.reactivex.Single

abstract class BaseSingler<T : Any> : BaseInteractor<T>() {

    protected abstract fun prepare(): Single<T>

    fun stream(): Single<T> = prepare().applySchedulers()

    private fun Single<T>.applySchedulers(): Single<T> {
        return compose { resultObservable ->
            resultObservable
                .subscribeOn(workScheduler)
                .observeOn(resultScheduler)
        }
    }
}
