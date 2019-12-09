package com.thefuntasty.mvvm.rxusecases.usecases

import io.reactivex.Maybe

/**
 * Base interactor which wraps [Maybe]. Instance of this
 * interactor can be simply executed in cooperation with
 * [com.thefuntasty.mvvm.rxusecases.disposables.MaybeDisposablesOwner] interface.
 *
 * Wrapped stream is subscribed on [io.reactivex.schedulers.Schedulers.io] and
 * observed on [io.reactivex.android.schedulers.AndroidSchedulers.mainThread]
 * by default. You may override these through [workScheduler] and
 * [resultScheduler] respectively.
 */
abstract class MayberUseCase<ARGS, T> : BaseUseCase() {

    /**
     * Prepares whole wrapped [Maybe] Rx stream. This method does not
     * subscribe to the stream.
     */
    protected abstract fun prepare(args: ARGS): Maybe<T>

    /**
     * Creates internal [Maybe] Rx stream, applies requested work
     * & result schedulers and exposes this stream as a [Maybe]. This method
     * is handy when you want to combine streams of multiple interactors.
     * For example:
     *
     * interactor_A.create(Unit).flatMap {
     *     interactor_B.create(Unit)
     * }
     */
    fun create(args: ARGS): Maybe<T> = prepare(args).applySchedulers()

    private fun Maybe<T>.applySchedulers(): Maybe<T> {
        return compose { resultObservable ->
            resultObservable
                .subscribeOn(workScheduler)
                .observeOn(resultScheduler)
        }
    }
}
