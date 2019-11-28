package com.thefuntasty.interactors.disposables

import com.thefuntasty.interactors.interactors.BaseMayber
import com.thefuntasty.interactors.wrapWithGlobalOnErrorLogger
import io.reactivex.Maybe
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign

/**
 * This interface gives your class ability to execute [BaseMayber] interactors
 * and automatically add resulting disposables to one composite disposable. You
 * may find handy to implement this interface in custom Presenters, ViewHolders etc.
 *
 * Consider using [DisposablesOwner] to support all of the basic RxJava 2 types.
 *
 * It is your responsibility to clear this composite disposable when all
 * running tasks should be stopped.
 */
interface MaybeDisposablesOwner {

    val disposables: CompositeDisposable

    /**
     * Executes the interactor and adds its disposable to
     * shared, automatically disposed, composite disposable. In case some
     * variant of [BaseMayber.execute] method has already been called
     * on this instance of [BaseMayber], previous one is disposed,
     * no matter what current state of internal Maybe is. This behavior
     * can be disabled by passing false to [MayberConfig.disposePrevious]
     * method.
     *
     * @param args Arguments used for initial interactor initialisation.
     * @return disposable of internal [Maybe]. This disposable is disposed
     * automatically. It might be used to dispose interactor when you need
     * to dispose it in advance on your own.
     */
    fun <ARGS, T> BaseMayber<ARGS, T>.execute(args: ARGS): Disposable = execute(args, { })

    /**
     * Executes the interactor and adds its disposable to
     * shared, automatically disposed, composite disposable. In case some
     * variant of [BaseMayber.execute] method has already been called
     * on this instance of [BaseMayber], previous one is disposed,
     * no matter what current state of internal Maybe is. This behavior
     * can be disabled by passing false to [MayberConfig.disposePrevious]
     * method.
     *
     * @param args Arguments used for initial interactor initialisation.
     * @param config [MayberConfig] used to process results of internal [Maybe].
     * @return disposable of internal [Maybe]. This disposable is disposed
     * automatically. It might be used to dispose interactor when you need
     * to dispose it in advance on your own.
     */
    fun <ARGS, T> BaseMayber<ARGS, T>.execute(
        args: ARGS,
        config: MayberConfig.Builder<T>.() -> Unit
    ): Disposable {
        val mayberConfig = MayberConfig.Builder<T>().run {
            config.invoke(this)
            return@run build()
        }

        if (mayberConfig.disposePrevious) {
            this@execute.currentDisposable?.dispose()
        }

        mayberConfig.onStart()
        val disposable = create(args)
            .subscribe(
                mayberConfig.onSuccess,
                wrapWithGlobalOnErrorLogger(mayberConfig.onError),
                mayberConfig.onComplete
            )

        this@execute.currentDisposable = disposable
        disposables += disposable

        return disposable
    }

    /**
     * Executes the [Maybe] and adds its disposable to
     * shared, automatically disposed, composite disposable.
     *
     * @param config [MayberConfig] used to process results of internal Maybe.
     * @return disposable of internal [Maybe]. It might be used to
     * dispose interactor when you need to dispose it in advance on your own.
     */
    fun <T : Any> Maybe<T>.executeStream(
        config: MayberConfig.Builder<T>.() -> Unit
    ): Disposable {
        val mayberConfig = MayberConfig.Builder<T>().run {
            config.invoke(this)
            return@run build()
        }

        mayberConfig.onStart()
        return subscribe(
            mayberConfig.onSuccess,
            wrapWithGlobalOnErrorLogger(mayberConfig.onError),
            mayberConfig.onComplete
        ).also {
            disposables += it
        }
    }
}

/**
 * Holds references to lambdas and some basic configuration
 * used to process results of Mayber interactor.
 * Use [MayberConfig.Builder] to construct this object.
 */
class MayberConfig<T> private constructor(
    val onStart: () -> Unit,
    val onSuccess: (T) -> Unit,
    val onComplete: () -> Unit,
    val onError: (Throwable) -> Unit,
    val disposePrevious: Boolean
) {
    /**
     * Constructs references to lambdas and some basic configuration
     * used to process results of Mayber interactor.
     */
    class Builder<T> {
        private var onStart: (() -> Unit)? = null
        private var onSuccess: ((T) -> Unit)? = null
        private var onComplete: (() -> Unit)? = null
        private var onError: ((Throwable) -> Unit)? = null
        private var disposePrevious = true

        /**
         * Set lambda which is called right before
         * internal Maybe is subscribed
         * @param onStart Lambda called right before Maybe is
         * subscribed.
         */
        fun onStart(onStart: () -> Unit) {
            this.onStart = onStart
        }

        /**
         * Set lambda which is called when onSuccess on
         * internal Maybe is called
         * @param onSuccess Lambda called when onSuccess is
         * emitted.
         */
        fun onSuccess(onSuccess: (T) -> Unit) {
            this.onSuccess = onSuccess
        }

        /**
         * Set lambda which is called when onComplete on
         * internal Maybe is called
         * @param onComplete Lambda called when onComplete is
         * emitted.
         */
        fun onComplete(onComplete: () -> Unit) {
            this.onComplete = onComplete
        }

        /**
         * Set lambda which is called when onError on
         * internal Maybe is called
         * @param onError Lambda called when onError is
         * emitted.
         */
        fun onError(onError: (Throwable) -> Unit) {
            this.onError = onError
        }

        /**
         * Set whether currently running internal Maybe
         * should be disposed when execute is called repeatedly.
         * @param disposePrevious True if currently running
         * internal Maybe should be disposed
         */
        fun disposePrevious(disposePrevious: Boolean) {
            this.disposePrevious = disposePrevious
        }

        fun build(): MayberConfig<T> {
            return MayberConfig(
                onStart ?: { },
                onSuccess ?: { },
                onComplete ?: { },
                onError ?: { throw it },
                disposePrevious
            )
        }
    }
}
