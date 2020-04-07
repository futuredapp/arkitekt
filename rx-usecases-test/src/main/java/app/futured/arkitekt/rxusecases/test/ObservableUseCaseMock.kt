package app.futured.arkitekt.rxusecases.test

import app.futured.arkitekt.rxusecases.disposables.ObservableDisposablesOwner
import app.futured.arkitekt.rxusecases.usecases.ObservableUseCase
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

/**
 * Mock [ObservableDisposablesOwner.execute] method.
 *
 * When the execute method will be called then the argument passed in [resultBlock] will be used as a result of mocked use case
 * and corresponding methods for the given use case will be called.
 * So when `Observable.just` will be passed then `onNext` will be called etc.
 *
 * Usage:
 * mockObservableUseCase.mockExecute(args = ...) { Observable.just(...) }
 */
inline fun <reified ARGS : Any, RETURN_VALUE, USE_CASE : ObservableUseCase<ARGS, RETURN_VALUE>> USE_CASE.mockExecute(args: ARGS, resultBlock: () -> Observable<RETURN_VALUE>) {
    mockCurrentDisposable()
    every { this@mockExecute.create(args) } returns resultBlock()
}

/**
 * Mock [ObservableDisposablesOwner.execute] method with `any()` matcher argument used as input argument.
 *
 * When the execute method will be called then the argument passed in [resultBlock] will be used as a result of mocked use case
 * and corresponding methods for the given use case will be called.
 * So when `Observable.just` will be passed then `onNext` will be called etc.
 *
 * Usage:
 * mockObservableUseCase.mockExecute(args = ...) { Observable.just(...) }
 */
inline fun <reified ARGS : Any, RETURN_VALUE, USE_CASE : ObservableUseCase<ARGS, RETURN_VALUE>> USE_CASE.mockExecute(resultBlock: () -> Observable<RETURN_VALUE>) {
    mockCurrentDisposable()
    every { this@mockExecute.create(any()) } returns resultBlock()
}

/**
 * Mock [ObservableDisposablesOwner.execute] method for use cases with nullable input argument
 *
 * When the execute method will be called then the argument passed in [resultBlock] will be used as a result of mocked use case
 * and corresponding methods for the given use case will be called.
 * So when `Observable.just` will be passed then `onNext` will be called etc.
 *
 * Usage:
 * mockObservableUseCase.mockExecuteNullable(args = ...) { Observable.just(...) }
 */
inline fun <reified ARGS : Any?, RETURN_VALUE, USE_CASE : ObservableUseCase<ARGS?, RETURN_VALUE>> USE_CASE.mockExecuteNullable(args: ARGS, resultBlock: () -> Observable<RETURN_VALUE>) {
    mockCurrentDisposable()
    every { this@mockExecuteNullable.create(args) } returns resultBlock()
}

/**
 * Mock [ObservableDisposablesOwner.execute] method for use cases with nullable input argument
 * and `any()` matcher argument used as input argument.
 *
 * When the execute method will be called then the argument passed in [resultBlock] will be used as a result of mocked use case
 * and corresponding methods for the given use case will be called.
 * So when `Observable.just` will be passed then `onNext` will be called etc.
 *
 * Usage:
 * mockObservableUseCase.mockExecuteNullable(args = ...) { Observable.just(...) }
 */
inline fun <reified ARGS : Any, RETURN_VALUE, USE_CASE : ObservableUseCase<ARGS?, RETURN_VALUE>> USE_CASE.mockExecuteNullable(resultBlock: () -> Observable<RETURN_VALUE>) {
    mockCurrentDisposable()
    every { this@mockExecuteNullable.create(any()) } returns resultBlock()
}

@PublishedApi
internal fun <RETURN_VALUE, USE_CASE : ObservableUseCase<*, RETURN_VALUE>> USE_CASE.mockCurrentDisposable() {
    every { this@mockCurrentDisposable getProperty "currentDisposable" } returns null
    every { this@mockCurrentDisposable setProperty "currentDisposable" value any<Disposable>() } just runs
}
