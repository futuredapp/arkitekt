package com.thefuntasty.interactors.interactors

import io.reactivex.Single

abstract class BaseSingler<ARGS, T> : BaseInteractor() {

    protected abstract fun prepare(args: ARGS): Single<T>

    fun create(args: ARGS): Single<T> = prepare(args).applySchedulers()

    private fun Single<T>.applySchedulers(): Single<T> {
        return compose { resultObservable ->
            resultObservable
                .subscribeOn(workScheduler)
                .observeOn(resultScheduler)
        }
    }
}
