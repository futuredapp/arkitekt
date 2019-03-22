package com.thefuntasty.interactors

import io.reactivex.Completable

/**
 * Base interactor which wraps [Completable]. Instance of this
 * interactor can be simply executed in cooperation with
 * [com.thefuntasty.interactors.disposables.DisposablesOwner] interface.
 *
 * Wrapped stream is subscribed on [io.reactivex.schedulers.Schedulers.io] and
 * observed on [io.reactivex.android.schedulers.AndroidSchedulers.mainThread]
 * by default. You may override these through [workScheduler] and
 * [resultScheduler] respectively.
 */
abstract class BaseCompletabler : BaseInteractor<Unit>() {

    /**
     * Prepares whole wrapped [Completable] Rx stream. This method do not
     * subscribes to the stream.
     */
    protected abstract fun prepare(): Completable

    /**
     * Creates internal [Completable] Rx stream, applies requested work
     * & result schedulers and exposes this stream as a [Completable]. Method
     * is handy when you want to combine streams of multiple interactors.
     * For example:
     *
     * interactor_A.stream().flatMap {
     *     interactor_B.stream()
     * }
     */
    fun stream(): Completable = prepare().applySchedulers()

    private fun Completable.applySchedulers(): Completable {
        return compose { resultObservable ->
            resultObservable
                .subscribeOn(workScheduler)
                .observeOn(resultScheduler)
        }
    }
}
