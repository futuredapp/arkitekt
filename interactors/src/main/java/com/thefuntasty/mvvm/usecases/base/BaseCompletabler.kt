package com.thefuntasty.mvvm.usecases.base

import io.reactivex.Completable

/**
 * Base interactor which wraps [Completable]. Instance of this
 * interactor can be simply executed in cooperation with
 * [com.thefuntasty.mvvm.usecases.disposables.CompletableDisposablesOwner] interface.
 *
 * Wrapped stream is subscribed on [io.reactivex.schedulers.Schedulers.io] and
 * observed on [io.reactivex.android.schedulers.AndroidSchedulers.mainThread]
 * by default. You may override these through [workScheduler] and
 * [resultScheduler] respectively.
 */
abstract class BaseCompletabler<ARGS> : BaseUseCase() {

    /**
     * Prepares whole wrapped [Completable] Rx stream. This method does not
     * subscribe to the stream.
     */
    protected abstract fun prepare(args: ARGS): Completable

    /**
     * Creates internal [Completable] Rx stream, applies requested work
     * & result schedulers and exposes this stream as a [Completable]. This method
     * is handy when you want to combine streams of multiple interactors.
     * For example:
     *
     * interactor_A.create(Unit).andThen(interactor_B.stream(Unit))
     */
    fun create(args: ARGS): Completable = prepare(args).applySchedulers()

    private fun Completable.applySchedulers(): Completable {
        return compose { resultObservable ->
            resultObservable
                .subscribeOn(workScheduler)
                .observeOn(resultScheduler)
        }
    }
}
