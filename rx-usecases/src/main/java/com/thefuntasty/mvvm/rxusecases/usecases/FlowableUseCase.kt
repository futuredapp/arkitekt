package com.thefuntasty.mvvm.rxusecases.usecases

import io.reactivex.Flowable

/**
 * Base use case which wraps [Flowable]. Instance of this
 * use case can be simply executed in cooperation with
 * [com.thefuntasty.mvvm.rxusecases.disposables.FlowableDisposablesOwner] interface.
 *
 * Wrapped stream is subscribed on [io.reactivex.schedulers.Schedulers.io] and
 * observed on [io.reactivex.android.schedulers.AndroidSchedulers.mainThread]
 * by default. You may override these through [workScheduler] and
 * [resultScheduler] respectively.
 */
abstract class FlowableUseCase<ARGS, T> : BaseUseCase() {

    /**
     * Prepares whole wrapped [Flowable] Rx stream. This method does not
     * subscribe to the stream.
     */
    protected abstract fun prepare(args: ARGS): Flowable<T>

    /**
     * Creates internal [Flowable] Rx stream, applies requested work
     * & result schedulers and exposes this stream as a [Flowable]. This method
     * is handy when you want to combine streams of multiple use cases.
     * For example:
     *
     * usecase_A.create(Unit).flatMap {
     *     usecase_B.create(Unit)
     * }
     */
    fun create(args: ARGS): Flowable<T> = prepare(args).applySchedulers()

    private fun Flowable<T>.applySchedulers(): Flowable<T> {
        return compose { resultObservable ->
            resultObservable
                .subscribeOn(workScheduler)
                .observeOn(resultScheduler)
        }
    }
}
