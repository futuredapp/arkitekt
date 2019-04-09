package com.thefuntasty.interactors

import io.reactivex.Single

/**
 * Base interactor which wraps [Single]. Instance of this
 * interactor can be simply executed in cooperation with
 * [com.thefuntasty.interactors.disposables.DisposablesOwner] interface.
 *
 * Wrapped stream is subscribed on [io.reactivex.schedulers.Schedulers.io] and
 * observed on [io.reactivex.android.schedulers.AndroidSchedulers.mainThread]
 * by default. You may override these through [workScheduler] and
 * [resultScheduler] respectively.
 */
abstract class BaseSingler<T : Any> : BaseInteractor<T>() {

    /**
     * Prepares whole wrapped [Single] Rx stream. This method does not
     * subscribe to the stream.
     */
    protected abstract fun prepare(): Single<T>

    /**
     * Creates internal [Single] Rx stream, applies requested work
     * & result schedulers and exposes this stream as a [Single]. This method
     * is handy when you want to combine streams of multiple interactors.
     * For example:
     *
     * interactor_A.stream().flatMap {
     *     interactor_B.stream()
     * }
     */
    fun stream(): Single<T> = prepare().applySchedulers()

    private fun Single<T>.applySchedulers(): Single<T> {
        return compose { resultObservable ->
            resultObservable
                .subscribeOn(workScheduler)
                .observeOn(resultScheduler)
        }
    }
}
