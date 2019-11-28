package com.thefuntasty.interactors.disposables

import com.thefuntasty.interactors.interactors.BaseFlowabler
import com.thefuntasty.interactors.wrapWithGlobalOnErrorLogger
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign

/**
 * This interface gives your class ability to execute [BaseFlowabler] interactors
 * and automatically add resulting disposables to one composite disposable. You
 * may find handy to implement this interface in custom Presenters, ViewHolders etc.
 *
 * Consider using [DisposablesOwner] to support all of the basic RxJava 2 types.
 *
 * It is your responsibility to clear this composite disposable when all
 * running tasks should be stopped.
 */
interface FlowableDisposablesOwner {

    val disposables: CompositeDisposable

    /**
     * Executes the interactor and adds its disposable to
     * shared, automatically disposed, composite disposable. In case some
     * variant of [BaseFlowabler.execute] method has already been called
     * on this instance of [BaseFlowabler], previous one is disposed,
     * no matter what current state of internal Flowable is. This behavior
     * can be disabled by passing false to [FlowablerConfig.disposePrevious]
     * method.
     *
     * @param args Arguments used for initial interactor initialisation.
     * @return disposable of internal [Flowable]. This disposable is disposed
     * automatically. It might be used to dispose interactor when you need
     * to dispose it in advance on your own.
     */
    fun <ARGS, T> BaseFlowabler<ARGS, T>.execute(args: ARGS): Disposable = execute(args, { })

    /**
     * Executes the interactor and adds its disposable to
     * shared, automatically disposed, composite disposable. In case some
     * variant of [BaseFlowabler.execute] method has already been called
     * on this instance of [BaseFlowabler], previous one is disposed,
     * no matter what current state of internal Flowable is. This behavior
     * can be disabled by passing false to [FlowablerConfig.disposePrevious]
     * method.
     *
     * @param args Arguments used for initial interactor initialisation.
     * @param config [FlowablerConfig] used to process results of internal [Flowable].
     * @return disposable of internal [Flowable]. This disposable is disposed
     * automatically. It might be used to dispose interactor when you need
     * to dispose it in advance on your own.
     */
    fun <ARGS, T> BaseFlowabler<ARGS, T>.execute(
        args: ARGS,
        config: FlowablerConfig.Builder<T>.() -> Unit
    ): Disposable {
        val flowablerConfig = FlowablerConfig.Builder<T>().run {
            config.invoke(this)
            return@run build()
        }

        if (flowablerConfig.disposePrevious) {
            this@execute.currentDisposable?.dispose()
        }

        flowablerConfig.onStart()
        val disposable = create(args)
            .subscribe(
                flowablerConfig.onNext,
                wrapWithGlobalOnErrorLogger(flowablerConfig.onError),
                flowablerConfig.onComplete
            )

        this@execute.currentDisposable = disposable
        disposables += disposable

        return disposable
    }

    /**
     * Executes the [Flowable] and adds its disposable to
     * shared, automatically disposed, composite disposable.
     *
     * @param config [FlowablerConfig] used to process results of internal Flowable.
     * @return disposable of internal [Flowable]. It might be used to
     * dispose interactor when you need to dispose it in advance on your own.
     */
    fun <T : Any> Flowable<T>.executeStream(
        config: FlowablerConfig.Builder<T>.() -> Unit
    ): Disposable {
        val flowablerConfig = FlowablerConfig.Builder<T>().run {
            config.invoke(this)
            return@run build()
        }

        flowablerConfig.onStart()
        return subscribe(
            flowablerConfig.onNext,
            wrapWithGlobalOnErrorLogger(flowablerConfig.onError),
            flowablerConfig.onComplete
        ).also {
            disposables += it
        }
    }
}

/**
 * Holds references to lambdas and some basic configuration
 * used to process results of Flowabler interactor.
 * Use [FlowablerConfig.Builder] to construct this object.
 */
class FlowablerConfig<T> private constructor(
    val onStart: () -> Unit,
    val onNext: (T) -> Unit,
    val onComplete: () -> Unit,
    val onError: (Throwable) -> Unit,
    val disposePrevious: Boolean
) {
    class Builder<T> {
        private var onStart: (() -> Unit)? = null
        private var onNext: ((T) -> Unit)? = null
        private var onComplete: (() -> Unit)? = null
        private var onError: ((Throwable) -> Unit)? = null
        private var disposePrevious = true

        /**
         * Set lambda which is called right before
         * internal Flowable is subscribed
         * @param onStart Lambda called right before Flowable is
         * subscribed.
         */
        fun onStart(onStart: () -> Unit) {
            this.onStart = onStart
        }

        /**
         * Set lambda which is called when onNext on
         * internal Flowable is called
         * @param onNext Lambda called when onNext is
         * emitted.
         */
        fun onNext(onNext: (T) -> Unit) {
            this.onNext = onNext
        }

        /**
         * Set lambda which is called when onComplete on
         * internal Flowable is called
         * @param onComplete Lambda called when onComplete is
         * emitted.
         */
        fun onComplete(onComplete: () -> Unit) {
            this.onComplete = onComplete
        }

        /**
         * Set lambda which is called when onError on
         * internal Flowable is called
         * @param onError Lambda called when onError is
         * emitted.
         */
        fun onError(onError: (Throwable) -> Unit) {
            this.onError = onError
        }

        /**
         * Set whether currently running internal Flowable
         * should be disposed when execute is called repeatedly.
         * @param disposePrevious True if currently running
         * internal Flowable should be disposed
         */
        fun disposePrevious(disposePrevious: Boolean) {
            this.disposePrevious = disposePrevious
        }

        fun build(): FlowablerConfig<T> {
            return FlowablerConfig(
                onStart ?: { },
                onNext ?: { },
                onComplete ?: { },
                onError ?: { throw it },
                disposePrevious
            )
        }
    }
}
