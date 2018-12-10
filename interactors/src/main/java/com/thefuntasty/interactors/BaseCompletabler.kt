package com.thefuntasty.interactors

import io.reactivex.Completable

abstract class BaseCompletabler : BaseInteractor<Unit>() {

    protected abstract fun prepare(): Completable

    open fun stream(): Completable = prepare().applySchedulers()

    private fun Completable.applySchedulers(): Completable {
        return compose { resultObservable ->
            resultObservable
                .subscribeOn(workScheduler)
                .observeOn(resultScheduler)
        }
    }
}
