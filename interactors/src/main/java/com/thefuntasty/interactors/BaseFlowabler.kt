package com.thefuntasty.interactors

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

abstract class BaseFlowabler<T : Any> : BaseInteractor<T>() {

    protected abstract fun create(): Flowable<T>

    internal fun getStream(): Flowable<T> {
        return create().applySchedulers()
    }

    internal fun Flowable<T>.applySchedulers(): Flowable<T> {
        return compose { resultObservable ->
            resultObservable
                .subscribeOn(getWorkScheduler())
                .observeOn(getResultScheduler())
        }
    }

    fun <OUT : Any, B : BaseFlowabler<OUT>> chain(
        chainedFlowabler: B,
        onResult: B.(T) -> Unit = { }
    ): BaseFlowabler<OUT> {
        return simpleFlowabler {
            this@BaseFlowabler
                .getStream()
                .flatMap { result ->
                    chainedFlowabler.also { onResult(chainedFlowabler, result) }.getStream()
                }
        }
    }

    fun <OUT : Any, B : BaseSingler<OUT>> chain(
        chainedSingler: B,
        onResult: B.(T) -> Unit = { }
    ): BaseFlowabler<OUT> {
        return simpleFlowabler {
            this@BaseFlowabler
                .getStream()
                .flatMapSingle { result ->
                    chainedSingler.also { onResult(chainedSingler, result) }.getStream()
                }
        }
    }

    fun <B : BaseCompletabler> chain(
        chainedCompletabler: B,
        onResult: B.(T) -> Unit = { }
    ): BaseFlowabler<Unit> {
        return simpleFlowabler {
            this@BaseFlowabler
                .getStream()
                .flatMap { result ->
                    chainedCompletabler.also {
                        onResult(chainedCompletabler, result)
                    }.getStream().andThen(Flowable.just(Unit))
                }
        }
    }

    fun <OUT : Any, B : BaseObservabler<OUT>> chain(
        chainedObservabler: B,
        backpressureStrategy: BackpressureStrategy,
        onResult: B.(T) -> Unit = { }
    ): BaseFlowabler<OUT> {
        return simpleFlowabler {
            this@BaseFlowabler
                .getStream()
                .flatMap { result ->
                    chainedObservabler.also {
                        onResult(chainedObservabler, result)
                    }.getStream().toFlowable(backpressureStrategy)
                }
        }
    }
}
