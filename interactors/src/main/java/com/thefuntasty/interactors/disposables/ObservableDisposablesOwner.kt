package com.thefuntasty.interactors.disposables

import com.thefuntasty.interactors.interactors.BaseObservabler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign

interface ObservableDisposablesOwner : BaseDisposableOwner {

    val disposables: CompositeDisposable

    fun <ARGS, T> BaseObservabler<ARGS, T>.execute(args: ARGS): Disposable = execute(args, { })

    fun <ARGS, T> BaseObservabler<ARGS, T>.execute(
        args: ARGS,
        config: ObservablerConfig.Builder<T>.() -> Unit
    ): Disposable {
        val observablerResult = ObservablerConfig.Builder<T>().run {
            config.invoke(this)
            return@run build()
        }

        if (observablerResult.disposePrevious) {
            this@execute.currentDisposable?.dispose()
        }

        val disposable = create(args)
            .subscribe(
                observablerResult.onNext,
                wrapWithGlobalOnErrorLogger(observablerResult.onError),
                observablerResult.onComplete
            )

        this@execute.currentDisposable = disposable
        disposables += disposable

        return disposable
    }
}

data class ObservablerConfig<T> constructor(
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

        fun build(): ObservablerConfig<T> {
            return ObservablerConfig(
                onNext ?: { },
                onComplete ?: { },
                onError ?: { throw it },
                disposePrevious
            )
        }
    }
}
