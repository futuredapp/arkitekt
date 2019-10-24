package com.thefuntasty.interactors.interactors

import io.reactivex.Completable

abstract class BaseCompletabler<ARGS> : BaseInteractor() {

    protected abstract fun prepare(args: ARGS): Completable

    fun create(args: ARGS): Completable = prepare(args).applySchedulers()

    private fun Completable.applySchedulers(): Completable {
        return compose { resultObservable ->
            resultObservable
                .subscribeOn(workScheduler)
                .observeOn(resultScheduler)
        }
    }
}
