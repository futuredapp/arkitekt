package com.thefuntasty.interactors.disposables

import com.thefuntasty.interactors.interactors.BaseSingler
import com.thefuntasty.interactors.wrapWithGlobalOnErrorLogger
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign

/**
 * This interface gives your class ability to execute [BaseSingler] interactors
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
     * Executes the interactor and adds its disposable to
     * shared, automatically disposed, composite disposable. In case some
     * variant of [BaseSingler.execute] method has already been called
     * on this instance of [BaseSingler], previous one is disposed,
     * no matter what current state of internal Single is.
     * Use [Single.executeStream] if you want to run one
     * [BaseSingler] multiple times simultaneously. This behavior
     * can be disabled by passing false to [SinglerConfig.disposePrevious]
     * method.
     *
     * @return disposable of internal [Single]. This disposable is disposed
     * automatically. It might be used to dispose interactor when you need
     * to dispose it in advance on your own.
     */
    // variant A of execute without params
    fun <T> BaseSingler<Unit, T>.execute(result: SinglerConfig.Builder<T>.() -> Unit): Disposable {
        return execute(Unit,result)
    }

    // variant B of execute without params
    fun <T> BaseSingler<Unit, T>.executeNoArgs(result: SinglerConfig.Builder<T>.() -> Unit): Disposable {
        return execute(Unit,result)
    }

    /**
     * Executes the interactor and adds its disposable to
     * shared, automatically disposed, composite disposable. In case some
     * variant of [BaseSingler.execute] method has already been called
     * on this instance of [BaseSingler], previous one is disposed,
     * no matter what current state of internal Single is.
     * Use [Single.executeStream] if you want to run one
     * [BaseSingler] multiple times simultaneously. This behavior
     * can be disabled by passing false to [SinglerConfig.disposePrevious]
     * method.
     *
     * @param args Arguments used for initial interactor initialization.
     * @param config [SinglerConfig] used to process results of internal [Single].
     * @return disposable of internal [Single]. This disposable is disposed
     * automatically. It might be used to dispose interactor when you need
     * to dispose it in advance on your own.
     */
    fun <ARGS, T> BaseSingler<ARGS, T>.execute(
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

        val disposable = create(args).subscribe(
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
     * dispose interactor when you need to dispose it in advance on your own.
     */
    fun <T : Any> Single<T>.executeStream(
        config: SinglerConfig.Builder<T>.() -> Unit
    ): Disposable {
        val singlerConfig = SinglerConfig.Builder<T>().run {
            config.invoke(this)
            return@run build()
        }

        return subscribe(
            singlerConfig.onSuccess,
            wrapWithGlobalOnErrorLogger(singlerConfig.onError)
        ).also {
            disposables += it
        }
    }
}

/**
 * Holds references to lambdas and some basic configuration
 * used to process results of Singler interactor.
 * Use [SinglerConfig.Builder] to construct this object.
 */
class SinglerConfig<T> private constructor(
    val onSuccess: (T) -> Unit,
    val onError: (Throwable) -> Unit,
    val disposePrevious: Boolean
) {
    /**
     * Constructs references to lambdas and some basic configuration
     * used to process results of Singler interactor.
     */
    class Builder<T> {
        private var onSuccess: ((T) -> Unit)? = null
        private var onError: ((Throwable) -> Unit)? = null
        private var disposePrevious = true

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
                onSuccess ?: { },
                onError ?: { throw it },
                disposePrevious
            )
        }
    }
}
