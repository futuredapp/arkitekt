package com.thefuntasty.interactors.disposables

import com.thefuntasty.interactors.interactors.BaseSingler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign

interface SingleDisposablesOwner {

    val disposables: CompositeDisposable

    infix fun <ARGS, T> BaseSingler<ARGS, T>.executeWith(args: ARGS): Disposable {
        return execute(args, { })
    }

    fun <ARGS, T> BaseSingler<ARGS, T>.execute(args: ARGS): Disposable {
        return execute(args, { })
    }

    fun <ARGS, T> BaseSingler<ARGS, T>.execute(args: ARGS, result: SinglerResult.Builder<T>.() -> Unit): Disposable {
        val singlerResult = SinglerResult.Builder<T>().run {
            result.invoke(this)
            return@run build()
        }

        if (singlerResult.disposePrevious) {
            this@execute.currentDisposable?.dispose()
        }

        val disposable = create(args).subscribe(singlerResult.onSuccess, singlerResult.onError)

        this@execute.currentDisposable = disposable
        disposables += disposable

        return disposable
    }
}

data class SinglerResult<T> constructor(
    val onSuccess: (T) -> Unit,
    val onError: (Throwable) -> Unit,
    val disposePrevious: Boolean
) {
    class Builder<T> {
        private var onSuccess: ((T) -> Unit)? = null
        private var onError: ((Throwable) -> Unit)? = null
        private var disposePrevious = true

        fun onSuccess(onSuccess: (T) -> Unit) {
            this.onSuccess = onSuccess
        }

        fun onError(onError: (Throwable) -> Unit) {
            this.onError = onError
        }

        fun disposePrevious(disposePrevious: Boolean) {
            this.disposePrevious = disposePrevious
        }

        fun build(): SinglerResult<T> {
            return SinglerResult(
                onSuccess ?: { },
                onError ?: { throw it },
                disposePrevious
            )
        }
    }
}
