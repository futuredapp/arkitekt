package com.thefuntasty.interactors.disposables

import com.thefuntasty.interactors.interactors.BaseFlowabler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign

interface FlowableDisposablesOwner : BaseDisposableOwner {

    val disposables: CompositeDisposable

    fun <ARGS, T> BaseFlowabler<ARGS, T>.execute(args: ARGS): Disposable = execute(args, { })

    fun <ARGS, T> BaseFlowabler<ARGS, T>.execute(
        args: ARGS,
        result: FlowablerResult.Builder<T>.() -> Unit
    ): Disposable {
        val flowablerResult = FlowablerResult.Builder<T>().run {
            result.invoke(this)
            return@run build()
        }

        if (flowablerResult.disposePrevious) {
            this@execute.currentDisposable?.dispose()
        }

        val disposable = create(args)
            .subscribe(
                flowablerResult.onNext,
                wrapWithGlobalOnErrorLogger(flowablerResult.onError),
                flowablerResult.onComplete
            )

        this@execute.currentDisposable = disposable
        disposables += disposable

        return disposable
    }
}

data class FlowablerResult<T> constructor(
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

        fun build(): FlowablerResult<T> {
            return FlowablerResult(
                onNext ?: { },
                onComplete ?: { },
                onError ?: { throw it },
                disposePrevious
            )
        }
    }
}
