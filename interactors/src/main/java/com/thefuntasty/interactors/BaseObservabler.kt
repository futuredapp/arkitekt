package com.thefuntasty.interactors

import io.reactivex.Observable

/**
 * Base interactor which wraps [Observable]. Instance of this
 * interactor can be simply executed in cooperation with
 * [com.thefuntasty.interactors.disposables.DisposablesOwner] interface.
 *
 * Wrapped stream is subscribed on [io.reactivex.schedulers.Schedulers.io] and
 * observed on [io.reactivex.android.schedulers.AndroidSchedulers.mainThread]
 * by default. You may override these through [workScheduler] and
 * [resultScheduler] respectively.
 */
abstract class BaseObservabler<T : Any> : BaseInteractor<T>() {

    /**
     * Prepares whole wrapped [Observable] Rx stream. This method does not
     * subscribe to the stream.
     */
    protected abstract fun prepare(): Observable<T>

    /**
     * Creates internal [Observable] Rx stream, applies requested work
     * & result schedulers and exposes this stream as a [Observable]. This method
     * is handy when you want to combine streams of multiple interactors.
     * For example:
     *
     * interactor_A.stream().flatMap {
     *     interactor_B.stream()
     * }
     */
    fun stream(): Observable<T> = prepare().applySchedulers()

    private fun Observable<T>.applySchedulers(): Observable<T> {
        return compose { resultObservable ->
            resultObservable
                .subscribeOn(workScheduler)
                .observeOn(resultScheduler)
        }
    }
}
