package com.thefuntasty.interactors

import io.reactivex.BackpressureStrategy
import io.reactivex.Observable

abstract class BaseObservabler<T : Any> : BaseInteractor<T>() {

    protected abstract fun create(): Observable<T>

    internal fun getStream(): Observable<T> {
        return create().applySchedulers()
    }

    internal fun Observable<T>.applySchedulers(): Observable<T> {
        return compose { resultObservable ->
            resultObservable
                .subscribeOn(getWorkScheduler())
                .observeOn(getResultScheduler())
        }
    }

    fun <OUT : Any, B : BaseObservabler<OUT>> chain(
        chainedObservabler: B,
        onResult: B.(T) -> Unit = { }
    ): BaseObservabler<OUT> {
        return simpleObservabler {
            this@BaseObservabler
                .getStream()
                .flatMap { t ->
                    chainedObservabler.also { onResult(chainedObservabler, t) }.getStream()
                }
        }
    }

    fun <OUT : Any, B : BaseFlowabler<OUT>> chain(
        chainedFlowabler: B,
        backpressureStrategy: BackpressureStrategy = BackpressureStrategy.BUFFER,
        onResult: B.(T) -> Unit = { }
    ): BaseFlowabler<OUT> {
        return simpleFlowabler {
            this@BaseObservabler
                .getStream()
                .toFlowable(backpressureStrategy)
                .flatMap { t ->
                    chainedFlowabler.also { onResult(chainedFlowabler, t) }.getStream()
                }
        }
    }

    fun <OUT : Any, B : BaseSingler<OUT>> chain(
        chainedSingler: B,
        onResult: B.(T) -> Unit = { }
    ): BaseObservabler<OUT> {
        return simpleObservabler {
            this@BaseObservabler
                .getStream()
                .flatMapSingle { t ->
                    chainedSingler.also { onResult(chainedSingler, t) }.getStream()
                }
        }
    }

    fun <B : BaseCompletabler> chain(
        chainedCompletabler: B,
        onResult: B.(T) -> Unit = { }
    ): BaseObservabler<Unit> {
        return simpleObservabler {
            this@BaseObservabler
                .getStream()
                .flatMap { t ->
                    chainedCompletabler.also {
                        onResult(chainedCompletabler, t)
                    }.getStream().andThen(Observable.just(Unit))
                }
        }
    }
}
