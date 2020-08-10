package app.futured.arkitekt.rxusecases.disposables

import app.futured.arkitekt.rxusecases.usecases.FlowableUseCase
import app.futured.arkitekt.rxusecases.wrapWithGlobalOnErrorLogger
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign

/**
 * This interface gives your class ability to execute [FlowableUseCase] use cases
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
     * Executes the use case and adds its disposable to
     * shared, automatically disposed, composite disposable. In case some
     * variant of [FlowableUseCase.execute] method has already been called
     * on this instance of [FlowableUseCase], previous one is disposed,
     * no matter what current state of internal Flowable is. This behavior
     * can be disabled by passing false to [FlowableUseCaseConfig.disposePrevious]
     * method.
     *
     * @param config [FlowablerConfig] used to process results of internal [Flowable].
     * @return disposable of internal [Flowable]. This disposable is disposed
     * automatically. It might be used to dispose use case when you need
     * to dispose it in advance on your own.
     */
    fun <T> FlowableUseCase<Unit, T>.execute(config: FlowableUseCaseConfig.Builder<T>.() -> Unit): Disposable =
        execute(Unit, config)

    /**
     * Executes the use case and adds its disposable to
     * shared, automatically disposed, composite disposable. In case some
     * variant of [FlowableUseCase.execute] method has already been called
     * on this instance of [FlowableUseCase], previous one is disposed,
     * no matter what current state of internal Flowable is. This behavior
     * can be disabled by passing false to [FlowableUseCaseConfig.disposePrevious]
     * method.
     *
     * @param args Arguments used for initial use case initialisation.
     * @param config [FlowableUseCaseConfig] used to process results of internal [Flowable].
     * @return disposable of internal [Flowable]. This disposable is disposed
     * automatically. It might be used to dispose use case when you need
     * to dispose it in advance on your own.
     */
    fun <ARGS, T> FlowableUseCase<ARGS, T>.execute(
        args: ARGS,
        config: FlowableUseCaseConfig.Builder<T>.() -> Unit
    ): Disposable {
        val flowablerConfig = FlowableUseCaseConfig.Builder<T>().run {
            config.invoke(this)
            return@run build()
        }

        if (flowablerConfig.disposePrevious) {
            this@execute.currentDisposable?.dispose()
        }

        val disposable = create(args)
            .doOnSubscribe { flowablerConfig.onStart() }
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
     * @param config [FlowableUseCaseConfig] used to process results of internal Flowable.
     * @return disposable of internal [Flowable]. It might be used to
     * dispose use case when you need to dispose it in advance on your own.
     */
    fun <T : Any> Flowable<T>.executeStream(
        config: FlowableUseCaseConfig.Builder<T>.() -> Unit
    ): Disposable {
        val flowablerConfig = FlowableUseCaseConfig.Builder<T>().run {
            config.invoke(this)
            return@run build()
        }

        return doOnSubscribe { flowablerConfig.onStart() }
            .subscribe(
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
 * used to process results of Flowabler use case.
 * Use [FlowableUseCaseConfig.Builder] to construct this object.
 */
class FlowableUseCaseConfig<T> private constructor(
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

        fun build(): FlowableUseCaseConfig<T> {
            return FlowableUseCaseConfig(
                onStart ?: { },
                onNext ?: { },
                onComplete ?: { },
                onError ?: { throw it },
                disposePrevious
            )
        }
    }
}
