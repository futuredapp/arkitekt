package com.thefuntasty.mvvm.rxusecases.disposables

import com.thefuntasty.mvvm.rxusecases.usecases.CompletableUseCase
import com.thefuntasty.mvvm.rxusecases.wrapWithGlobalOnErrorLogger
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign

/**
 * This interface gives your class ability to execute [CompletableUseCase] use cases
 * and automatically add resulting disposables to one composite disposable. You
 * may find handy to implement this interface in custom Presenters, ViewHolders etc.
 *
 * Consider using [DisposablesOwner] to support all of the basic RxJava 2 types.
 *
 * It is your responsibility to clear this composite disposable when all
 * running tasks should be stopped.
 */
interface CompletableDisposablesOwner {

    val disposables: CompositeDisposable

    /**
     * Executes the use case and adds its disposable to
     * shared, automatically disposed, composite disposable. In case some
     * variant of [CompletableUseCase.execute] method has already been called
     * on this instance of [CompletableUseCase], previous one is disposed,
     * no matter what current state of internal Completable is. This behavior
     * can be disabled by passing false to [CompletableUseCaseConfig.disposePrevious]
     * method.
     *
     * @param args Arguments used for initial use case initialisation.
     * @return disposable of internal [Completable]. This disposable is disposed
     * automatically. It might be used to dispose use case when you need
     * to dispose it in advance on your own.
     */
    fun <ARGS> CompletableUseCase<ARGS>.execute(args: ARGS): Disposable = execute(args, { })

    /**
     * Executes the use case and adds its disposable to
     * shared, automatically disposed, composite disposable. In case some
     * variant of [CompletableUseCase.execute] method has already been called
     * on this instance of [CompletableUseCase], previous one is disposed,
     * no matter what current state of internal Completable is. This behavior
     * can be disabled by passing false to [CompletableUseCaseConfig.disposePrevious]
     * method.
     *
     * @param args Arguments used for initial use case initialisation.
     * @param config [CompletableUseCaseConfig] used to process results of internal [Completable].
     * @return disposable of internal [Completable]. This disposable is disposed
     * automatically. It might be used to dispose use case when you need
     * to dispose it in advance on your own.
     */
    fun <ARGS> CompletableUseCase<ARGS>.execute(
        args: ARGS,
        config: CompletableUseCaseConfig.Builder.() -> Unit
    ): Disposable {
        val completablerConfig = CompletableUseCaseConfig.Builder().run {
            config.invoke(this)
            return@run build()
        }

        if (completablerConfig.disposePrevious) {
            this@execute.currentDisposable?.dispose()
        }

        val disposable = create(args)
            .doOnSubscribe { completablerConfig.onStart() }
            .subscribe(
                completablerConfig.onComplete,
                wrapWithGlobalOnErrorLogger(completablerConfig.onError)
            )

        this@execute.currentDisposable = disposable
        disposables += disposable

        return disposable
    }

    /**
     * Executes the [Completable] and adds its disposable to
     * shared, automatically disposed, composite disposable.
     *
     * @param config [CompletableUseCaseConfig] used to process results of internal Completable.
     * @return disposable of internal [Completable]. It might be used to
     * dispose use case when you need to dispose it in advance on your own.
     */
    fun Completable.executeStream(
        config: CompletableUseCaseConfig.Builder.() -> Unit
    ): Disposable {
        val completablerConfig = CompletableUseCaseConfig.Builder().run {
            config.invoke(this)
            return@run build()
        }

        return doOnSubscribe { completablerConfig.onStart() }
            .subscribe(
                completablerConfig.onComplete,
                wrapWithGlobalOnErrorLogger(completablerConfig.onError)
            ).also {
                disposables += it
            }
    }
}

/**
 * Holds references to lambdas and some basic configuration
 * used to process results of Completabler use case.
 * Use [CompletableUseCaseConfig.Builder] to construct this object.
 */
class CompletableUseCaseConfig private constructor(
    val onStart: () -> Unit,
    val onComplete: () -> Unit,
    val onError: (Throwable) -> Unit,
    val disposePrevious: Boolean
) {
    /**
     * Constructs references to lambdas and some basic configuration
     * used to process results of Completabler use case.
     */
    class Builder {
        private var onStart: (() -> Unit)? = null
        private var onComplete: (() -> Unit)? = null
        private var onError: ((Throwable) -> Unit)? = null
        private var disposePrevious = true

        /**
         * Set lambda which is called right before
         * internal Completable is subscribed
         * @param onStart Lambda called right before Completable is
         * subscribed.
         */
        fun onStart(onStart: () -> Unit) {
            this.onStart = onStart
        }

        /**
         * Set lambda which is called when onComplete on
         * internal Completable is called
         * @param onComplete Lambda called when onComplete is
         * emitted.
         */
        fun onComplete(onComplete: () -> Unit) {
            this.onComplete = onComplete
        }

        /**
         * Set lambda which is called when onError on
         * internal Completable is called
         * @param onError Lambda called when onError is
         * emitted.
         */
        fun onError(onError: (Throwable) -> Unit) {
            this.onError = onError
        }

        /**
         * Set whether currently running internal Completable
         * should be disposed when execute is called repeatedly.
         * @param disposePrevious True if currently running
         * internal Completable should be disposed
         */
        fun disposePrevious(disposePrevious: Boolean) {
            this.disposePrevious = disposePrevious
        }

        fun build(): CompletableUseCaseConfig {
            return CompletableUseCaseConfig(
                onStart ?: { },
                onComplete ?: { },
                onError ?: { throw it },
                disposePrevious
            )
        }
    }
}
