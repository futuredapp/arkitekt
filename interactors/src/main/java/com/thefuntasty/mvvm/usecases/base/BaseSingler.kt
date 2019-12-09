package com.thefuntasty.mvvm.usecases.base

import io.reactivex.Single

/**
 * Base interactor which wraps [Single]. Instance of this
 * interactor can be simply executed in cooperation with
 * [com.thefuntasty.mvvm.usecases.disposables.SingleDisposablesOwner] interface.
 *
 * Wrapped stream is subscribed on [io.reactivex.schedulers.Schedulers.io] and
 * observed on [io.reactivex.android.schedulers.AndroidSchedulers.mainThread]
 * by default. You may override these through [workScheduler] and
 * [resultScheduler] respectively.
 */
abstract class BaseSingler<ARGS, T> : BaseUseCase() {

    /**
     * Prepares whole wrapped [Single] Rx stream. This method does not
     * subscribe to the stream.
     */
    protected abstract fun prepare(args: ARGS): Single<T>

    /**
     * Creates internal [Single] Rx stream, applies requested work
     * & result schedulers and exposes this stream as a [Single]. This method
     * is handy when you want to combine streams of multiple interactors.
     * For example:
     *
     * interactor_A.create().flatMap {
     *     interactor_B.create()
     * }
     */
    fun create(args: ARGS): Single<T> = prepare(args).applySchedulers()

    private fun Single<T>.applySchedulers(): Single<T> {
        return compose { resultObservable ->
            resultObservable
                .subscribeOn(workScheduler)
                .observeOn(resultScheduler)
        }
    }
}
