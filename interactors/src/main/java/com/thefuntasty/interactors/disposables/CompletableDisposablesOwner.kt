package com.thefuntasty.interactors.disposables

import com.thefuntasty.interactors.interactors.BaseCompletabler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign

interface CompletableDisposablesOwner : BaseDisposableOwner {

    val disposables: CompositeDisposable

    infix fun <ARGS> BaseCompletabler<ARGS>.executeWith(args: ARGS): Disposable {
        return execute(args, { })
    }

    fun <ARGS> BaseCompletabler<ARGS>.execute(args: ARGS): Disposable {
        return execute(args, { })
    }

    fun <ARGS> BaseCompletabler<ARGS>.execute(args: ARGS, result: CompletablerResult.Builder.() -> Unit): Disposable {
        val completablerResult = CompletablerResult.Builder().run {
            result.invoke(this)
            return@run build()
        }

        if (completablerResult.disposePrevious) {
            this@execute.currentDisposable?.dispose()
        }

        val disposable = create(args)
            .subscribe(
                completablerResult.onComplete,
                wrapWithGlobalOnErrorLogger(completablerResult.onError)
            )

        this@execute.currentDisposable = disposable
        disposables += disposable

        return disposable
    }
}

data class CompletablerResult constructor(
    val onComplete: () -> Unit,
    val onError: (Throwable) -> Unit,
    val disposePrevious: Boolean
) {
    class Builder {
        private var onComplete: (() -> Unit)? = null
        private var onError: ((Throwable) -> Unit)? = null
        private var disposePrevious = true

        fun onComplete(onComplete: () -> Unit) {
            this.onComplete = onComplete
        }

        fun onError(onError: (Throwable) -> Unit) {
            this.onError = onError
        }

        fun disposePrevious(disposePrevious: Boolean) {
            this.disposePrevious = disposePrevious
        }

        fun build(): CompletablerResult {
            return CompletablerResult(
                onComplete ?: { },
                onError ?: { throw it },
                disposePrevious
            )
        }
    }
}
