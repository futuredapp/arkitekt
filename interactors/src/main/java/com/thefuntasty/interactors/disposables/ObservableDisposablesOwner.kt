package com.thefuntasty.interactors.disposables

import com.thefuntasty.interactors.interactors.BaseObservabler
import com.thefuntasty.interactors.wrapWithGlobalOnErrorLogger
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign

/**
 * This interface gives your class ability to execute [BaseObservabler] interactors
 * and automatically add resulting disposables to one composite disposable. You
 * may find handy to implement this interface in custom Presenters, ViewHolders etc.
 *
 * Consider using [DisposablesOwner] to support all of the basic RxJava 2 types.
 *
 * It is your responsibility to clear this composite disposable when all
 * running tasks should be stopped.
 */
interface ObservableDisposablesOwner {

    val disposables: CompositeDisposable

    /**
     * Executes the interactor and adds its disposable to
     * shared, automatically disposed, composite disposable. In case some
     * variant of [BaseObservabler.execute] method has already been called
     * on this instance of [BaseObservabler], previous one is disposed,
     * no matter what current state of internal Observable is. This behavior
     * can be disabled by passing false to [ObservablerConfig.disposePrevious]
     * method.
     *
     * @param args Arguments used for initial interactor initialisation.
     * @return disposable of internal [Observable]. This disposable is disposed
     * automatically. It might be used to dispose interactor when you need
     * to dispose it in advance on your own.
     */
    fun <ARGS, T> BaseObservabler<ARGS, T>.execute(args: ARGS): Disposable = execute(args, { })

    /**
     * Executes the interactor and adds its disposable to
     * shared, automatically disposed, composite disposable. In case some
     * variant of [BaseObservabler.execute] method has already been called
     * on this instance of [BaseObservabler], previous one is disposed,
     * no matter what current state of internal Observable is. This behavior
     * can be disabled by passing false to [ObservablerConfig.disposePrevious]
     * method.
     *
     * @param args Arguments used for initial interactor initialisation.
     * @param config [ObservablerConfig] used to process results of internal [Observable].
     * @return disposable of internal [Observable]. This disposable is disposed
     * automatically. It might be used to dispose interactor when you need
     * to dispose it in advance on your own.
     */
    fun <ARGS, T> BaseObservabler<ARGS, T>.execute(
        args: ARGS,
        config: ObservablerConfig.Builder<T>.() -> Unit
    ): Disposable {
        val observablerConfig = ObservablerConfig.Builder<T>().run {
            config.invoke(this)
            return@run build()
        }

        if (observablerConfig.disposePrevious) {
            this@execute.currentDisposable?.dispose()
        }

        val disposable = create(args)
            .doOnSubscribe { observablerConfig.onStart() }
            .subscribe(
                observablerConfig.onNext,
                wrapWithGlobalOnErrorLogger(observablerConfig.onError),
                observablerConfig.onComplete
            )

        this@execute.currentDisposable = disposable
        disposables += disposable

        return disposable
    }

    /**
     * Executes the [Observable] and adds its disposable to
     * shared, automatically disposed, composite disposable.
     *
     * @param config [ObservablerConfig] used to process results of internal Observable.
     * @return disposable of internal [Observable]. It might be used to
     * dispose interactor when you need to dispose it in advance on your own.
     */
    fun <T : Any> Observable<T>.executeStream(
        config: ObservablerConfig.Builder<T>.() -> Unit
    ): Disposable {
        val observablerConfig = ObservablerConfig.Builder<T>().run {
            config.invoke(this)
            return@run build()
        }

        return doOnSubscribe { observablerConfig.onStart() }
            .subscribe(
                observablerConfig.onNext,
                wrapWithGlobalOnErrorLogger(observablerConfig.onError),
                observablerConfig.onComplete
            ).also {
                disposables += it
            }
    }
}

/**
 * Holds references to lambdas and some basic configuration
 * used to process results of Observabler interactor.
 * Use [ObservablerConfig.Builder] to construct this object.
 */
class ObservablerConfig<T> private constructor(
    val onStart: () -> Unit,
    val onNext: (T) -> Unit,
    val onComplete: () -> Unit,
    val onError: (Throwable) -> Unit,
    val disposePrevious: Boolean
) {
    /**
     * Constructs references to lambdas and some basic configuration
     * used to process results of Observabler interactor.
     */
    class Builder<T> {
        private var onStart: (() -> Unit)? = null
        private var onNext: ((T) -> Unit)? = null
        private var onComplete: (() -> Unit)? = null
        private var onError: ((Throwable) -> Unit)? = null
        private var disposePrevious = true

        /**
         * Set lambda which is called right before
         * internal Observable is subscribed
         * @param onStart Lambda called right before Observable is
         * subscribed.
         */
        fun onStart(onStart: () -> Unit) {
            this.onStart = onStart
        }

        /**
         * Set lambda which is called when onNext on
         * internal Observable is called
         * @param onNext Lambda called when onNext is
         * emitted.
         */
        fun onNext(onNext: (T) -> Unit) {
            this.onNext = onNext
        }

        /**
         * Set lambda which is called when onComplete on
         * internal Observable is called
         * @param onComplete Lambda called when onComplete is
         * emitted.
         */
        fun onComplete(onComplete: () -> Unit) {
            this.onComplete = onComplete
        }

        /**
         * Set lambda which is called when onError on
         * internal Observable is called
         * @param onError Lambda called when onError is
         * emitted.
         */
        fun onError(onError: (Throwable) -> Unit) {
            this.onError = onError
        }

        /**
         * Set whether currently running internal Observable
         * should be disposed when execute is called repeatedly.
         * @param disposePrevious True if currently running
         * internal Observable should be disposed
         */
        fun disposePrevious(disposePrevious: Boolean) {
            this.disposePrevious = disposePrevious
        }

        fun build(): ObservablerConfig<T> {
            return ObservablerConfig(
                onStart ?: { },
                onNext ?: { },
                onComplete ?: { },
                onError ?: { throw it },
                disposePrevious
            )
        }
    }
}
