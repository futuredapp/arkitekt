package com.thefuntasty.interactors.disposables

import androidx.annotation.RestrictTo
import com.thefuntasty.interactors.BaseCompletabler
import com.thefuntasty.interactors.BaseFlowabler
import com.thefuntasty.interactors.BaseObservabler
import com.thefuntasty.interactors.BaseSingler
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.TestObserver
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subscribers.TestSubscriber

/**
 * This interface gives your class ability to execute [BaseFlowabler], [BaseSingler],
 * [BaseCompletabler], [BaseObservabler] interactors and automatically add
 * resulting disposables to one composite disposable. You may find handy to implement
 * this interface in custom Presenters, ViewHolders etc.
 *
 * It is your responsibility to clear this composite disposable when all
 * running tasks should be stopped.
 */
interface DisposablesOwner {

    companion object {
        /**
         * Error lambda used as a default one when custom lambda
         * is not provided in [execute] and [executeStream] methods.
         */
        private val onErrorLambda: (Throwable) -> Unit = { throw it }
    }

    val disposables: CompositeDisposable

    /**
     * Executes the interactor and adds its disposable to
     * shared, automatically disposed, composite disposable. In case some
     * variant of [BaseFlowabler.execute] method has already been called
     * on this instance of [BaseFlowabler], previous one is disposed,
     * no matter what current state of internal Flowable is.
     * Use [Flowable.executeStream] if you want to run one
     * [BaseFlowabler] multiple times simultaneously.
     *
     * @param onNext Lambda executed when internal Flowable emits
     * onNext event
     * @return disposable of internal [Flowable]. This disposable is disposed
     * automatically. It might be used to dispose interactor when you need
     * to dispose it in advance on your own.
     */
    fun <T : Any> BaseFlowabler<T>.execute(onNext: (T) -> Unit) = execute(onNext, onError = onErrorLambda)

    /**
     * Executes the interactor and adds its disposable to
     * shared, automatically disposed, composite disposable. In case some
     * variant of [BaseFlowabler.execute] method has already been called
     * on this instance of [BaseFlowabler], previous one is disposed,
     * no matter what current state of internal Flowable is.
     * Use [Flowable.executeStream] if you want to run one
     * [BaseFlowabler] multiple times simultaneously.
     *
     * @param onNext Lambda executed when internal [Flowable] emits
     * onNext event
     * @param onError Lambda executed when internal [Flowable] emits
     * onError event. [onErrorLambda] used as a default value.
     * @param onNext Lambda executed when internal [Flowable] emits
     * onComplete event. Empty lambda by default.
     * @return disposable of internal [Flowable]. This disposable is disposed
     * automatically. It might be used to dispose interactor when you need
     * to dispose it in advance on your own.
     */
    fun <T : Any> BaseFlowabler<T>.execute(
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit = onErrorLambda,
        onComplete: () -> Unit = { }
    ): Disposable {
        this@execute.currentDisposable?.dispose()

        val disposable = stream()
            .subscribe(onNext, onError, onComplete)

        this@execute.currentDisposable = disposable
        disposables += disposable

        return disposable
    }

    /**
     * Executes the interactor and adds its disposable to
     * shared, automatically disposed, composite disposable. In case some
     * variant of [BaseObservabler.execute] method has already been called
     * on this instance of [BaseObservabler], previous one is disposed,
     * no matter what current state of internal Flowable is.
     * Use [Observable.executeStream] if you want to run one
     * [BaseObservabler] multiple times simultaneously.
     *
     * @param onNext Lambda executed when internal [Observable] emits
     * onNext event
     * @return disposable of internal [Observable]. This disposable is disposed
     * automatically. It might be used to dispose interactor when you need
     * to dispose it in advance on your own.
     */
    fun <T : Any> BaseObservabler<T>.execute(onNext: (T) -> Unit) = execute(onNext, onError = onErrorLambda)

    /**
     * Executes the interactor and adds its disposable to
     * shared, automatically disposed, composite disposable. In case some
     * variant of [BaseObservabler.execute] method has already been called
     * on this instance of [BaseObservabler], previous one is disposed,
     * no matter what current state of internal Observable is.
     * Use [Observable.executeStream] if you want to run one
     * [BaseObservabler] multiple times simultaneously.
     *
     * @param onNext Lambda executed when internal [Observable] emits
     * onNext event
     * @param onError Lambda executed when internal [Observable] emits
     * onError event. [onErrorLambda] used as a default value.
     * @param onNext Lambda executed when internal [Observable] emits
     * onComplete event. Empty lambda by default.
     * @return disposable of internal [Observable]. This disposable is disposed
     * automatically. It might be used to dispose interactor when you need
     * to dispose it in advance on your own.
     */
    fun <T : Any> BaseObservabler<T>.execute(
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit = onErrorLambda,
        onComplete: () -> Unit = { }
    ): Disposable {
        this@execute.currentDisposable?.dispose()

        val disposable = stream()
            .subscribe(onNext, onError, onComplete)

        this@execute.currentDisposable = disposable
        disposables += disposable

        return disposable
    }

    /**
     * Executes the interactor and adds its disposable to
     * shared, automatically disposed, composite disposable. In case some
     * variant of [BaseSingler.execute] method has already been called
     * on this instance of [BaseSingler], previous one is disposed,
     * no matter what current state of internal Single is.
     * Use [Single.executeStream] if you want to run one
     * [BaseSingler] multiple times simultaneously.
     *
     * @param onSuccess Lambda executed when internal [Single] emits
     * onSuccess event
     * @return disposable of internal [Single]. It might be used to
     * dispose interactor when you need to dispose it in advance on your own.
     */
    fun <T : Any> BaseSingler<T>.execute(onSuccess: (T) -> Unit) = execute(onSuccess, onError = onErrorLambda)

    /**
     * Executes the interactor and adds its disposable to
     * shared, automatically disposed, composite disposable. In case some
     * variant of [BaseSingler.execute] method has already been called
     * on this instance of [BaseSingler], previous one is disposed,
     * no matter what current state of internal Single is.
     * Use [Single.executeStream] if you want to run one
     * [BaseSingler] multiple times simultaneously.
     *
     * @param onSuccess Lambda executed when internal [Single] emits
     * onSuccess event
     * @param onError Lambda executed when internal [Single] emits
     * onError event
     * @return disposable of internal [Single]. It might be used to
     * dispose interactor when you need to dispose it in advance on your own.
     */
    fun <T : Any> BaseSingler<T>.execute(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit = onErrorLambda
    ): Disposable {
        this@execute.currentDisposable?.dispose()

        val disposable = stream()
            .subscribe(onSuccess, onError)

        this@execute.currentDisposable = disposable
        disposables += disposable

        return disposable
    }

    /**
     * Executes the interactor and adds its disposable to
     * shared, automatically disposed, composite disposable. In case some
     * variant of [BaseSingler.execute] method has already been called
     * on this instance of [BaseCompletabler], previous one is disposed,
     * no matter what current state of internal Completable is.
     * Use [Single.executeStream] if you want to run one
     * [BaseCompletabler] multiple times simultaneously.
     *
     * @param onComplete Lambda executed when internal [Completable] emits
     * onComplete event
     * @return disposable of internal [Completable]. It might be used to
     * dispose interactor when you need to dispose it in advance on your own.
     */
    fun BaseCompletabler.execute(onComplete: () -> Unit) = execute(onComplete, onError = onErrorLambda)

    /**
     * Executes the interactor and adds its disposable to
     * shared, automatically disposed, composite disposable. In case some
     * variant of [BaseCompletabler.execute] method has already been called
     * on this instance of [BaseCompletabler], previous one is disposed,
     * no matter what current state of internal Completable is.
     * Use [Completable.executeStream] if you want to run one
     * [BaseCompletabler] multiple times simultaneously.
     *
     * @param onComplete Lambda executed when internal [Completable] emits
     * onComplete event
     * @param onError Lambda executed when internal [Completable] emits
     * onError event
     * @return disposable of internal [Completable]. It might be used to
     * dispose interactor when you need to dispose it in advance on your own.
     */
    fun BaseCompletabler.execute(
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit = onErrorLambda
    ): Disposable {
        this@execute.currentDisposable?.dispose()

        val disposable = stream()
            .subscribe(onComplete, onError)

        this@execute.currentDisposable = disposable
        disposables += disposable

        return disposable
    }

    /**
     * Executes the [Flowable] and adds its disposable to
     * shared, automatically disposed, composite disposable.
     *
     * @param onNext Lambda executed when internal [Flowable] emits
     * onNext event
     * @return disposable of internal [Flowable]. This disposable is disposed
     * automatically. It might be used to dispose [Flowable] when you need
     * to dispose it in advance on your own.
     */
    fun <T : Any> Flowable<T>.executeStream(onNext: (T) -> Unit) = executeStream(onNext, onError = onErrorLambda)

    /**
     * Executes the [Flowable] and adds its disposable to
     * shared, automatically disposed, composite disposable.
     *
     * @param onNext Lambda executed when internal [Flowable] emits
     * onNext event
     * @param onError Lambda executed when internal [Flowable] emits
     * onError event. [onErrorLambda] used as a default value.
     * @param onComplete Lambda executed when internal [Flowable] emits
     * onComplete event. Empty lambda by default.
     * @return disposable of internal [Flowable]. This disposable is disposed
     * automatically. It might be used to dispose [Flowable] when you need
     * to dispose it in advance on your own.
     */
    fun <T : Any> Flowable<T>.executeStream(
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit = onErrorLambda,
        onComplete: () -> Unit = { }
    ): Disposable {
        return subscribe(onNext, onError, onComplete).also {
            disposables += it
        }
    }

    /**
     * Executes the [Observable] and adds its disposable to
     * shared, automatically disposed, composite disposable.
     *
     * @param onNext Lambda executed when internal [Observable] emits
     * onNext event
     * @return disposable of internal [Observable]. This disposable is disposed
     * automatically. It might be used to dispose interactor when you need
     * to dispose it in advance on your own.
     */
    fun <T : Any> Observable<T>.executeStream(onNext: (T) -> Unit) = executeStream(onNext, onError = onErrorLambda)

    /**
     * Executes the [Observable] and adds its disposable to
     * shared, automatically disposed, composite disposable.
     *
     * @param onNext Lambda executed when internal [Observable] emits
     * onNext event
     * @param onError Lambda executed when internal [Observable] emits
     * onError event. [onErrorLambda] used as a default value.
     * @param onNext Lambda executed when internal [Observable] emits
     * onComplete event. Empty lambda by default.
     * @return disposable of internal [Observable]. This disposable is disposed
     * automatically. It might be used to dispose interactor when you need
     * to dispose it in advance on your own.
     */
    fun <T : Any> Observable<T>.executeStream(
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit = onErrorLambda,
        onComplete: () -> Unit = { }
    ): Disposable {
        return subscribe(onNext, onError, onComplete).also {
            disposables += it
        }
    }

    /**
     * Executes the [Single] and adds its disposable to
     * shared, automatically disposed, composite disposable.
     *
     * @param onSuccess Lambda executed when internal [Single] emits
     * onSuccess event
     * @return disposable of internal [Single]. It might be used to
     * dispose interactor when you need to dispose it in advance on your own.
     */
    fun <T : Any> Single<T>.executeStream(onSuccess: (T) -> Unit) = executeStream(onSuccess, onError = onErrorLambda)

    /**
     * Executes the [Single] and adds its disposable to
     * shared, automatically disposed, composite disposable.
     *
     * @param onSuccess Lambda executed when internal [Single] emits
     * onSuccess event
     * @param onError Lambda executed when internal [Single] emits
     * onError event
     * @return disposable of internal [Single]. It might be used to
     * dispose interactor when you need to dispose it in advance on your own.
     */
    fun <T : Any> Single<T>.executeStream(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit = onErrorLambda
    ): Disposable {
        return subscribe(onSuccess, onError).also {
            disposables += it
        }
    }

    /**
     * Executes the [Completable] and adds its disposable to
     * shared, automatically disposed, composite disposable.
     *
     * @param onComplete Lambda executed when internal [Completable] emits
     * onComplete event
     * @return disposable of internal [Completable]. It might be used to
     * dispose interactor when you need to dispose it in advance on your own.
     */
    fun Completable.executeStream(onComplete: () -> Unit) = executeStream(onComplete, onError = onErrorLambda)

    /**
     * Executes the [Completable] and adds its disposable to
     * shared, automatically disposed, composite disposable.
     *
     * @param onComplete Lambda executed when internal [Completable] emits
     * onComplete event
     * @param onError Lambda executed when internal [Completable] emits
     * onError event
     * @return disposable of internal [Completable]. It might be used to
     * dispose interactor when you need to dispose it in advance on your own.
     */
    fun Completable.executeStream(
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit = onErrorLambda
    ): Disposable {
        return subscribe(onComplete, onError).also {
            disposables += it
        }
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun <T : Any> BaseFlowabler<T>.executeSubscriber(subscriber: TestSubscriber<T>) {
        stream().subscribe(subscriber)

        disposables += subscriber
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun <T : Any> BaseSingler<T>.executeSubscriber(subscriber: TestSubscriber<T>) {
        stream()
            .toFlowable()
            .subscribe(subscriber)

        disposables += subscriber
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun <T : Any> BaseCompletabler.executeSubscriber(subscriber: TestSubscriber<T>) {
        stream()
            .toFlowable<T>()
            .subscribe(subscriber)

        disposables += subscriber
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun <T : Any> BaseObservabler<T>.executeSubscriber(observer: TestObserver<T>) {
        stream().subscribe(observer)

        disposables += observer
    }
}
