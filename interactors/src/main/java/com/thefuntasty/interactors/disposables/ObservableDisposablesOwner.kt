package com.thefuntasty.interactors.disposables

import com.thefuntasty.interactors.interactors.BaseObservabler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign

interface ObservableDisposablesOwner {

    val disposables: CompositeDisposable

    infix fun <ARGS, T> BaseObservabler<ARGS, T>.executeWith(args: ARGS): Disposable {
        return execute(args, { })
    }

    fun <ARGS, T> BaseObservabler<ARGS, T>.execute(args: ARGS): Disposable {
        return execute(args, { })
    }

    fun <ARGS, T> BaseObservabler<ARGS, T>.execute(args: ARGS, result: ObservablerResult.Builder<T>.() -> Unit): Disposable {
        val observablerResult = ObservablerResult.Builder<T>().run {
            result.invoke(this)
            return@run build()
        }

        if (observablerResult.disposePrevious) {
            this@execute.currentDisposable?.dispose()
        }

        val disposable = create(args)
            .subscribe(observablerResult.onNext, observablerResult.onError, observablerResult.onComplete)

        this@execute.currentDisposable = disposable
        disposables += disposable

        return disposable
    }
}

data class ObservablerResult<T> constructor(
    val onNext: (T) -> Unit,
    val onComplete: () -> Unit,
    val onError: (Throwable) -> Unit,
    val disposePrevious: Boolean
) {
    class Builder<T> {
        private var onNext: ((T) -> Unit)? = null
        private var onComplete: (() -> Unit)? = null
        private var onError: ((Throwable) -> Unit)? = null
        private var disposePrevious = true

        fun onNext(onNext: (T) -> Unit) {
            this.onNext = onNext
        }

        fun onComplete(onComplete: () -> Unit) {
            this.onComplete = onComplete
        }

        fun onError(onError: (Throwable) -> Unit) {
            this.onError = onError
        }

        fun disposePrevious(disposePrevious: Boolean) {
            this.disposePrevious = disposePrevious
        }

        fun build(): ObservablerResult<T> {
            return ObservablerResult(
                onNext ?: { },
                onComplete ?: { },
                onError ?: { throw it },
                disposePrevious
            )
        }
    }
}
