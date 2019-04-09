package com.thefuntasty.interactors

import io.reactivex.Flowable

/**
 * Base interactor which wraps [Flowable]. Instance of this
 * interactor can be simply executed in cooperation with
 * [com.thefuntasty.interactors.disposables.DisposablesOwner] interface.
 *
 * Wrapped stream is subscribed on [io.reactivex.schedulers.Schedulers.io] and
 * observed on [io.reactivex.android.schedulers.AndroidSchedulers.mainThread]
 * by default. You may override these through [workScheduler] and
 * [resultScheduler] respectively.
 */
abstract class BaseFlowabler<T : Any> : BaseInteractor<T>() {

    /**
     * Prepares whole wrapped [Flowable] Rx stream. This method does not
     * subscribe to the stream.
     */
    protected abstract fun prepare(): Flowable<T>

    /**
     * Creates internal [Flowable] Rx stream, applies requested work
     * & result schedulers and exposes this stream as a [Flowable]. This method
     * is handy when you want to combine streams of multiple interactors.
     * For example:
     *
     * interactor_A.stream().flatMap {
     *     interactor_B.stream()
     * }
     */
    fun stream(): Flowable<T> = prepare().applySchedulers()

    private fun Flowable<T>.applySchedulers(): Flowable<T> {
        return compose { resultObservable ->
            resultObservable
                .subscribeOn(workScheduler)
                .observeOn(resultScheduler)
        }
    }
}
