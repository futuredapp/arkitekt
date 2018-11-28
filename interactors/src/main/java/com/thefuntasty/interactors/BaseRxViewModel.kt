package com.thefuntasty.interactors

import androidx.annotation.VisibleForTesting
import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.ViewState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.TestObserver
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subscribers.TestSubscriber

abstract class BaseRxViewModel<S : ViewState> : BaseViewModel<S>() {

    private val onErrorLambda: (Throwable) -> Unit = { throw it }

    val disposables = CompositeDisposable()

    fun <T : Any> BaseFlowabler<T>.execute(onNext: (T) -> Unit) = execute(onNext, onError = onErrorLambda)

    fun <T : Any> BaseFlowabler<T>.execute(
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit = onErrorLambda,
        onComplete: () -> Unit = { }
    ) {
        val disposable = getStream()
            .applySchedulers()
            .subscribe(onNext, onError, onComplete)

        disposables += disposable
    }

    fun <T : Any> BaseObservabler<T>.execute(onNext: (T) -> Unit) = execute(onNext, onError = onErrorLambda)

    fun <T : Any> BaseObservabler<T>.execute(
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit = onErrorLambda,
        onComplete: () -> Unit = { }
    ) {
        val disposable = getStream()
            .applySchedulers()
            .subscribe(onNext, onError, onComplete)

        disposables += disposable
    }

    fun <T : Any> BaseSingler<T>.execute(onSuccess: (T) -> Unit) = execute(onSuccess, onError = onErrorLambda)

    fun <T : Any> BaseSingler<T>.execute(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit = onErrorLambda
    ) {
        val disposable = getStream()
            .applySchedulers()
            .subscribe(onSuccess, onError)

        disposables += disposable
    }

    fun BaseCompletabler.execute(onComplete: () -> Unit) = execute(onComplete, onError = onErrorLambda)

    fun BaseCompletabler.execute(
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit = onErrorLambda
    ) {
        val disposable = getStream()
            .applySchedulers()
            .subscribe(onComplete, onError)

        disposables += disposable
    }

    @VisibleForTesting
    fun <T : Any> BaseFlowabler<T>.executeSubscribe(subscriber: TestSubscriber<T>) {
        getStream()
            .applySchedulers()
            .subscribe(subscriber)

        disposables += subscriber
    }

    @VisibleForTesting
    fun <T : Any> BaseSingler<T>.executeSubscribe(subscriber: TestSubscriber<T>) {
        getStream()
            .applySchedulers()
            .toFlowable()
            .subscribe(subscriber)

        disposables += subscriber
    }

    @VisibleForTesting
    fun <T : Any> BaseCompletabler.executeSubscribe(subscriber: TestSubscriber<T>) {
        getStream()
            .applySchedulers()
            .toFlowable<T>()
            .subscribe(subscriber)

        disposables += subscriber
    }

    @VisibleForTesting
    fun <T : Any> BaseObservabler<T>.executeSubscribe(observer: TestObserver<T>) {
        getStream()
            .applySchedulers()
            .subscribe(observer)

        disposables += observer
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
