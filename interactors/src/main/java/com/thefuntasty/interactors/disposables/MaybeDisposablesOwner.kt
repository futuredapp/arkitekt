package com.thefuntasty.interactors.disposables

import com.thefuntasty.interactors.interactors.BaseMayber
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign

interface MaybeDisposablesOwner : BaseDisposableOwner {

    val disposables: CompositeDisposable

    fun <ARGS, T> BaseMayber<ARGS, T>.execute(args: ARGS): Disposable = execute(args, { })

    fun <ARGS, T> BaseMayber<ARGS, T>.execute(
        args: ARGS,
        config: MayberConfig.Builder<T>.() -> Unit
    ): Disposable {
        val mayberResult = MayberConfig.Builder<T>().run {
            config.invoke(this)
            return@run build()
        }

        if (mayberResult.disposePrevious) {
            this@execute.currentDisposable?.dispose()
        }

        val disposable = create(args)
            .subscribe(
                mayberResult.onSuccess,
                wrapWithGlobalOnErrorLogger(mayberResult.onError),
                mayberResult.onComplete
            )

        this@execute.currentDisposable = disposable
        disposables += disposable

        return disposable
    }
}

data class MayberConfig<T> constructor(
    val onSuccess: (T) -> Unit,
    val onComplete: () -> Unit,
    val onError: (Throwable) -> Unit,
    val disposePrevious: Boolean
) {
    class Builder<T> {
        private var onSuccess: ((T) -> Unit)? = null
        private var onComplete: (() -> Unit)? = null
        private var onError: ((Throwable) -> Unit)? = null
        private var disposePrevious = true

        fun onSuccess(onSuccess: (T) -> Unit) {
            this.onSuccess = onSuccess
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

        fun build(): MayberConfig<T> {
            return MayberConfig(
                onSuccess ?: { },
                onComplete ?: { },
                onError ?: { throw it },
                disposePrevious
            )
        }
    }
}
