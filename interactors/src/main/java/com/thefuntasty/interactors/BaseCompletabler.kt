package com.thefuntasty.interactors

import io.reactivex.Completable

abstract class BaseCompletabler : BaseInteractor<Unit>() {

    protected abstract fun create(): Completable

    fun getStream(): Completable {
        return create().applySchedulers()
    }

    internal fun Completable.applySchedulers(): Completable {
        return compose { resultObservable ->
            resultObservable
                .subscribeOn(getWorkScheduler())
                .observeOn(getResultScheduler())
        }
    }

    fun <B : BaseCompletabler> chain(
        chainedCompletabler: B,
        onResult: B.() -> Unit = { }
    ): BaseCompletabler {
        return simpleCompletabler {
            this@BaseCompletabler
                .getStream()
                .andThen(chainedCompletabler.also { onResult(chainedCompletabler) }.getStream())
        }
    }

    fun <OUT : Any, B : BaseSingler<OUT>> chain(
        chainedSingler: B,
        onResult: B.() -> Unit = { }
    ): BaseSingler<OUT> {
        return simpleSingler {
            this@BaseCompletabler
                .getStream()
                .andThen(chainedSingler.also { onResult(chainedSingler) }.getStream())
        }
    }

    fun <OUT : Any, B : BaseFlowabler<OUT>> chain(
        chainedFlowabler: B,
        onResult: B.() -> Unit = { }
    ): BaseFlowabler<OUT> {
        return simpleFlowabler {
            this@BaseCompletabler
                .getStream()
                .andThen(chainedFlowabler.also { onResult(chainedFlowabler) }.getStream())
        }
    }

    fun <OUT : Any, B : BaseObservabler<OUT>> chain(
        chainedObservabler: B,
        onResult: B.() -> Unit = { }
    ): BaseObservabler<OUT> {
        return simpleObservabler {
            this@BaseCompletabler
                .getStream()
                .andThen(chainedObservabler.also { onResult(chainedObservabler) }.getStream())
        }
    }
}
