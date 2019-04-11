package com.thefuntasty.interactors

import io.reactivex.Maybe

/**
 * Base interactor which wraps [Maybe]. Instance of this
 * interactor can be simply executed in cooperation with
 * [com.thefuntasty.interactors.disposables.DisposablesOwner] interface.
 *
 * Wrapped stream is subscribed on [io.reactivex.schedulers.Schedulers.io] and
 * observed on [io.reactivex.android.schedulers.AndroidSchedulers.mainThread]
 * by default. You may override these through [workScheduler] and
 * [resultScheduler] respectively.
 */
abstract class BaseMayber<T : Any> : BaseInteractor() {

    /**
     * Prepares whole wrapped [Maybe] Rx stream. This method does not
     * subscribe to the stream.
     */
    protected abstract fun prepare(): Maybe<T>

    /**
     * Creates internal [Maybe] Rx stream, applies requested work
     * & result schedulers and exposes this stream as a [Maybe]. This method
     * is handy when you want to combine streams of multiple interactors.
     * For example:
     *
     * interactor_A.stream().flatMap {
     *     interactor_B.stream()
     * }
     */
    fun stream(): Maybe<T> = prepare().applySchedulers()

    private fun Maybe<T>.applySchedulers(): Maybe<T> {
        return compose { resultObservable ->
            resultObservable
                .subscribeOn(workScheduler)
                .observeOn(resultScheduler)
        }
    }
}
