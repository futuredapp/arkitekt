package com.thefuntasty.interactors

import io.reactivex.Single

abstract class BaseSingler<T : Any> : BaseInteractor<T>() {
    protected abstract fun create(): Single<T>

    internal fun getStream(): Single<T> {
        return create().applySchedulers()
    }

    internal fun Single<T>.applySchedulers(): Single<T> {
        return compose { resultObservable ->
            resultObservable
                .subscribeOn(getWorkScheduler())
                .observeOn(getResultScheduler())
        }
    }

    fun <OUT : Any, B : BaseSingler<OUT>> chain(
        chainedSingler: B,
        onResult: B.(T) -> Unit = { }
    ): BaseSingler<OUT> {
        return simpleSingler {
            this@BaseSingler
                .getStream()
                .flatMap { result ->
                    chainedSingler.also { onResult(chainedSingler, result) }.getStream()
                }
        }
    }

    fun <OUT : Any, B : BaseFlowabler<OUT>> chain(
        chainedFlowabler: B,
        onResult: B.(T) -> Unit = { }
    ): BaseFlowabler<OUT> {
        return simpleFlowabler {
            this@BaseSingler
                .getStream()
                .flatMapPublisher { result ->
                    chainedFlowabler.also { onResult(chainedFlowabler, result) }.getStream()
                }
        }
    }

    fun <B : BaseCompletabler> chain(
        chainedCompletabler: B,
        onResult: B.(T) -> Unit = { }
    ): BaseCompletabler {
        return simpleCompletabler {
            this@BaseSingler
                .getStream()
                .flatMapCompletable { result ->
                    chainedCompletabler.also { onResult(chainedCompletabler, result) }.getStream()
                }
        }
    }

    fun <OUT : Any, B : BaseObservabler<OUT>> chain(
        chainedObservabler: B,
        onResult: B.(T) -> Unit = { }
    ): BaseObservabler<OUT> {
        return simpleObservabler {
            this@BaseSingler
                .getStream()
                .flatMapObservable { result ->
                    chainedObservabler.also { onResult(chainedObservabler, result) }.getStream()
                }
        }
    }
}
