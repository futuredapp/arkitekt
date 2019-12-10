package com.thefuntasty.mvvm.rxusecases.usecases

import io.reactivex.Observable

/**
 * Base use case which wraps [Observable]. Instance of this
 * use case can be simply executed in cooperation with
 * [com.thefuntasty.mvvm.rxusecases.disposables.ObservableDisposablesOwner] interface.
 *
 * Wrapped stream is subscribed on [io.reactivex.schedulers.Schedulers.io] and
 * observed on [io.reactivex.android.schedulers.AndroidSchedulers.mainThread]
 * by default. You may override these through [workScheduler] and
 * [resultScheduler] respectively.
 */
abstract class ObservableUseCase<ARGS, T> : BaseUseCase() {

    /**
     * Prepares whole wrapped [Observable] Rx stream. This method does not
     * subscribe to the stream.
     */
    protected abstract fun prepare(args: ARGS): Observable<T>

    /**
     * Creates internal [Observable] Rx stream, applies requested work
     * & result schedulers and exposes this stream as a [Observable]. This method
     * is handy when you want to combine streams of multiple use cases.
     * For example:
     *
     * usecase_A.create(Unit).flatMap {
     *     usecase_B.create(Unit)
     * }
     */
    fun create(args: ARGS): Observable<T> = prepare(args).applySchedulers()

    private fun Observable<T>.applySchedulers(): Observable<T> {
        return compose { resultObservable ->
            resultObservable
                .subscribeOn(workScheduler)
                .observeOn(resultScheduler)
        }
    }
}
