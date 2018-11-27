package com.thefuntasty.interactors

import androidx.annotation.VisibleForTesting
import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.ViewState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.Exceptions
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subscribers.TestSubscriber

abstract class BaseRxViewModel<S : ViewState> : BaseViewModel<S>() {

    private val onErrorLambda: (Throwable) -> Unit = { Exceptions.propagate(it) }

    val disposables = CompositeDisposable()

    fun <T : Any> BaseFlowabler<T>.execute(
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit = onErrorLambda,
        onComplete: () -> Unit = { }
    ) {
        lateinit var disposable: Disposable

        disposable = create()
            .applySchedulers()
            .doFinally { disposables.remove(disposable) }
            .subscribe(onNext, onError, onComplete)

        disposables += disposable
    }

    @VisibleForTesting
    fun <T : Any> BaseFlowabler<T>.executeSubscribe(subscriber: TestSubscriber<T>) {
        create()
            .applySchedulers()
            .subscribe(subscriber)

        disposables += subscriber
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
