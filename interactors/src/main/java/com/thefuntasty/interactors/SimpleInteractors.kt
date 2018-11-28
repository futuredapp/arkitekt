package com.thefuntasty.interactors

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

fun <T : Any> simpleFlowabler(createFlowableBlock: BaseFlowabler<T>.() -> Flowable<T>): BaseFlowabler<T> {
    return object : BaseFlowabler<T>() {
        override fun create(): Flowable<T> {
            return createFlowableBlock()
        }
    }
}

fun <T : Any> simpleSingler(createSingleBlock: BaseSingler<T>.() -> Single<T>): BaseSingler<T> {
    return object : BaseSingler<T>() {
        override fun create(): Single<T> {
            return createSingleBlock()
        }
    }
}

fun simpleCompletabler(createCompletableBlock: BaseCompletabler.() -> Completable): BaseCompletabler {
    return object : BaseCompletabler() {
        override fun create(): Completable {
            return createCompletableBlock()
        }
    }
}

fun <T : Any> simpleObservabler(createObservableBlock: BaseObservabler<T>.() -> Observable<T>): BaseObservabler<T> {
    return object : BaseObservabler<T>() {
        override fun create(): Observable<T> {
            return createObservableBlock()
        }
    }
}
