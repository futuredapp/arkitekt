package com.thefuntasty.mvvm.rxusecases.disposables

import com.thefuntasty.mvvm.rxusecases.usecases.SinglerUseCase
import com.thefuntasty.mvvm.rxusecases.wrapWithGlobalOnErrorLogger
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign

/**
 * This interface gives your class ability to execute [SinglerUseCase] use cases
 * and automatically add resulting disposables to one composite disposable. You
 * may find handy to implement this interface in custom Presenters, ViewHolders etc.
 *
 * Consider using [DisposablesOwner] to support all of the basic RxJava 2 types.
 *
 * It is your responsibility to clear this composite disposable when all
 * running tasks should be stopped.
 */
interface SingleDisposablesOwner {

    val disposables: CompositeDisposable

    /**
     * Executes the use case and adds its disposable to
     * shared, automatically disposed, composite disposable. In case some
     * variant of [SinglerUseCase.execute] method has already been called
     * on this instance of [SinglerUseCase], previous one is disposed,
     * no matter what current state of internal Single is.
     * Use [Single.executeStream] if you want to run one
     * [SinglerUseCase] multiple times simultaneously. This behavior
     * can be disabled by passing false to [SinglerConfig.disposePrevious]
     * method.
     *
     * @param args Arguments used for initial use case initialisation.
     * @return disposable of internal [Single]. This disposable is disposed
     * automatically. It might be used to dispose use case when you need
     * to dispose it in advance on your own.
     */
    fun <ARGS, T> SinglerUseCase<ARGS, T>.execute(args: ARGS): Disposable = execute(args, { })

    /**
     * Executes the use case and adds its disposable to
     * shared, automatically disposed, composite disposable. In case some
     * variant of [SinglerUseCase.execute] method has already been called
     * on this instance of [SinglerUseCase], previous one is disposed,
     * no matter what current state of internal Single is.
     * Use [Single.executeStream] if you want to run one
     * [SinglerUseCase] multiple times simultaneously. This behavior
     * can be disabled by passing false to [SinglerConfig.disposePrevious]
     * method.
     *
     * @param args Arguments used for initial use case initialization.
     * @param config [SinglerConfig] used to process results of internal [Single].
     * @return disposable of internal [Single]. This disposable is disposed
     * automatically. It might be used to dispose use case when you need
     * to dispose it in advance on your own.
     */
    fun <ARGS, T> SinglerUseCase<ARGS, T>.execute(
        args: ARGS,
        config: SinglerConfig.Builder<T>.() -> Unit
    ): Disposable {
        val singlerConfig = SinglerConfig.Builder<T>().run {
            config.invoke(this)
            return@run build()
        }

        if (singlerConfig.disposePrevious) {
            this@execute.currentDisposable?.dispose()
        }

        val disposable = create(args)
            .doOnSubscribe { singlerConfig.onStart() }
            .subscribe(
                singlerConfig.onSuccess,
                wrapWithGlobalOnErrorLogger(singlerConfig.onError)
            )

        this@execute.currentDisposable = disposable
        disposables += disposable

        return disposable
    }

    /**
     * Executes the [Single] and adds its disposable to
     * shared, automatically disposed, composite disposable.
     *
     * @param config [SinglerConfig] used to process results of internal Single.
     * @return disposable of internal [Single]. It might be used to
     * dispose use case when you need to dispose it in advance on your own.
     */
    fun <T : Any> Single<T>.executeStream(
        config: SinglerConfig.Builder<T>.() -> Unit
    ): Disposable {
        val singlerConfig = SinglerConfig.Builder<T>().run {
            config.invoke(this)
            return@run build()
        }

        return doOnSubscribe { singlerConfig.onStart() }
            .subscribe(
                singlerConfig.onSuccess,
                wrapWithGlobalOnErrorLogger(singlerConfig.onError)
            ).also {
                disposables += it
            }
    }
}

/**
 * Holds references to lambdas and some basic configuration
 * used to process results of Singler use case.
 * Use [SinglerConfig.Builder] to construct this object.
 */
class SinglerConfig<T> private constructor(
    val onStart: () -> Unit,
    val onSuccess: (T) -> Unit,
    val onError: (Throwable) -> Unit,
    val disposePrevious: Boolean
) {
    /**
     * Constructs references to lambdas and some basic configuration
     * used to process results of Singler use case.
     */
    class Builder<T> {
        private var onStart: (() -> Unit)? = null
        private var onSuccess: ((T) -> Unit)? = null
        private var onError: ((Throwable) -> Unit)? = null
        private var disposePrevious = true

        /**
         * Set lambda which is called right before
         * internal Single is subscribed
         * @param onStart Lambda called right before Single is
         * subscribed.
         */
        fun onStart(onStart: () -> Unit) {
            this.onStart = onStart
        }

        /**
         * Set lambda which is called when onSuccess on
         * internal Single is called
         * @param onSuccess Lambda called when onSuccess is
         * emitted.
         */
        fun onSuccess(onSuccess: (T) -> Unit) {
            this.onSuccess = onSuccess
        }

        /**
         * Set lambda which is called when onError on
         * internal Single is called
         * @param onError Lambda called when onError is
         * emitted.
         */
        fun onError(onError: (Throwable) -> Unit) {
            this.onError = onError
        }

        /**
         * Set whether currently running internal Single
         * should be disposed when execute is called repeatedly.
         * @param disposePrevious True if currently running
         * internal Single should be disposed
         */
        fun disposePrevious(disposePrevious: Boolean) {
            this.disposePrevious = disposePrevious
        }

        fun build(): SinglerConfig<T> {
            return SinglerConfig(
                onStart ?: { },
                onSuccess ?: { },
                onError ?: { throw it },
                disposePrevious
            )
        }
    }
}
