package com.thefuntasty.interactors

import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.ViewState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.exceptions.Exceptions
import io.reactivex.rxkotlin.plusAssign

abstract class BaseRxViewModel<S : ViewState> : BaseViewModel<S>() {

    private val onErrorLambda: (Throwable) -> Unit = { Exceptions.propagate(it) }

    val disposables = CompositeDisposable()

    fun <T : Any> BaseFlowabler<T>.execute(
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit = onErrorLambda,
        onComplete: () -> Unit = { }
    ) {
        disposables += create()
            .applySchedulers()
            .subscribe(onNext, onError, onComplete)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
