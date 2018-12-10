package com.thefuntasty.interactors

import androidx.annotation.VisibleForTesting
import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.ViewState
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
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
    ): Disposable {
        this@execute.currentDisposable?.dispose()

        val disposable = stream()
            .subscribe(onNext, onError, onComplete)

        this@execute.currentDisposable = disposable
        disposables += disposable

        return disposable
    }

    fun <T : Any> BaseObservabler<T>.execute(onNext: (T) -> Unit) = execute(onNext, onError = onErrorLambda)

    fun <T : Any> BaseObservabler<T>.execute(
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit = onErrorLambda,
        onComplete: () -> Unit = { }
    ): Disposable {
        this@execute.currentDisposable?.dispose()

        val disposable = stream()
            .subscribe(onNext, onError, onComplete)

        this@execute.currentDisposable = disposable
        disposables += disposable

        return disposable
    }

    fun <T : Any> BaseSingler<T>.execute(onSuccess: (T) -> Unit) = execute(onSuccess, onError = onErrorLambda)

    fun <T : Any> BaseSingler<T>.execute(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit = onErrorLambda
    ): Disposable {
        this@execute.currentDisposable?.dispose()

        val disposable = stream()
            .subscribe(onSuccess, onError)

        this@execute.currentDisposable = disposable
        disposables += disposable

        return disposable
    }

    fun BaseCompletabler.execute(onComplete: () -> Unit) = execute(onComplete, onError = onErrorLambda)

    fun BaseCompletabler.execute(
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit = onErrorLambda
    ): Disposable {
        this@execute.currentDisposable?.dispose()

        val disposable = stream()
            .subscribe(onComplete, onError)

        this@execute.currentDisposable = disposable
        disposables += disposable

        return disposable
    }

    fun <T : Any> Flowable<T>.executeStream(onNext: (T) -> Unit) = executeStream(onNext, onError = onErrorLambda)

    fun <T : Any> Flowable<T>.executeStream(
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit = onErrorLambda,
        onComplete: () -> Unit = { }
    ): Disposable {
        return subscribe(onNext, onError, onComplete).also {
            disposables += it
        }
    }

    fun <T : Any> Observable<T>.executeStream(onNext: (T) -> Unit) = executeStream(onNext, onError = onErrorLambda)

    fun <T : Any> Observable<T>.executeStream(
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit = onErrorLambda,
        onComplete: () -> Unit = { }
    ): Disposable {
        return subscribe(onNext, onError, onComplete).also {
            disposables += it
        }
    }

    fun <T : Any> Single<T>.executeStream(onSuccess: (T) -> Unit) = executeStream(onSuccess, onError = onErrorLambda)

    fun <T : Any> Single<T>.executeStream(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit = onErrorLambda
    ): Disposable {
        return subscribe(onSuccess, onError).also {
            disposables += it
        }
    }

    fun Completable.executeStream(onComplete: () -> Unit) = executeStream(onComplete, onError = onErrorLambda)

    fun Completable.executeStream(
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit = onErrorLambda
    ): Disposable {
        return subscribe(onComplete, onError).also {
            disposables += it
        }
    }

    @VisibleForTesting
    internal fun <T : Any> BaseFlowabler<T>.executeSubscriber(subscriber: TestSubscriber<T>) {
        stream().subscribe(subscriber)

        disposables += subscriber
    }

    @VisibleForTesting
    internal fun <T : Any> BaseSingler<T>.executeSubscriber(subscriber: TestSubscriber<T>) {
        stream()
            .toFlowable()
            .subscribe(subscriber)

        disposables += subscriber
    }

    @VisibleForTesting
    internal fun <T : Any> BaseCompletabler.executeSubscriber(subscriber: TestSubscriber<T>) {
        stream()
            .toFlowable<T>()
            .subscribe(subscriber)

        disposables += subscriber
    }

    @VisibleForTesting
    internal fun <T : Any> BaseObservabler<T>.executeSubscriber(observer: TestObserver<T>) {
        stream().subscribe(observer)

        disposables += observer
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
