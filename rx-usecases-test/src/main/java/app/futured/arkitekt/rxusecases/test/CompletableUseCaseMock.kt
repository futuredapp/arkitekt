package app.futured.arkitekt.rxusecases.test

import app.futured.arkitekt.rxusecases.disposables.CompletableDisposablesOwner
import app.futured.arkitekt.rxusecases.usecases.CompletableUseCase
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.reactivex.Completable
import io.reactivex.disposables.Disposable

/**
 * Mock [CompletableDisposablesOwner.execute] method.
 *
 * When the execute method will be called then the argument passed in [resultBlock] will be used as a result of mocked use case
 * and corresponding methods for the given use case will be called.
 * So when `Completable.complete` will be passed then `onComplete` will be called etc.
 *
 * Usage:
 * mockCompletableUseCase.mockExecute(args = ...) { Completable.complete() }
 */
inline fun <reified ARGS : Any, USE_CASE : CompletableUseCase<ARGS>> USE_CASE.mockExecute(args: ARGS, resultBlock: () -> Completable) {
    mockCurrentDisposable()
    every { this@mockExecute.create(args) } returns resultBlock()
}

/**
 * Mock [CompletableDisposablesOwner.execute] method with `any()` matcher argument used as input argument.
 *
 * When the execute method will be called then the argument passed in [resultBlock] will be used as a result of mocked use case
 * and corresponding methods for the given use case will be called.
 * So when `Completable.complete` will be passed then `onComplete` will be called etc.
 *
 * Usage:
 * mockCompletableUseCase.mockExecute(args = ...) { Completable.complete() }
 */
inline fun <reified ARGS : Any, USE_CASE : CompletableUseCase<ARGS>> USE_CASE.mockExecute(resultBlock: () -> Completable) {
    mockCurrentDisposable()
    every { this@mockExecute.create(any()) } returns resultBlock()
}

/**
 * Mock [CompletableDisposablesOwner.execute] method for use cases with nullable input argument.
 *
 * When the execute method will be called then the argument passed in [resultBlock] will be used as a result of mocked use case
 * and corresponding methods for the given use case will be called.
 * So when `Completable.complete` will be passed then `onComplete` will be called etc.
 *
 * Usage:
 * mockCompletableUseCase.mockExecuteNullable(args = ...) { Completable.complete() }
 */
inline fun <reified ARGS : Any?, USE_CASE : CompletableUseCase<ARGS?>> USE_CASE.mockExecuteNullable(args: ARGS, resultBlock: () -> Completable) {
    mockCurrentDisposable()
    every { this@mockExecuteNullable.create(args) } returns resultBlock()
}

/**
 * Mock [CompletableDisposablesOwner.execute] method for use cases with nullable input argument
 * and `any()` matcher argument used as input argument.
 *
 * When the execute method will be called then the argument passed in [resultBlock] will be used as a result of mocked use case
 * and corresponding methods for the given use case will be called.
 * So when `Completable.complete` will be passed then `onComplete` will be called etc.
 *
 * Usage:
 * mockCompletableUseCase.mockExecuteNullable(args = ...) { Completable.complete() }
 */
inline fun <reified ARGS : Any, USE_CASE : CompletableUseCase<ARGS?>> USE_CASE.mockExecuteNullable(resultBlock: () -> Completable) {
    mockCurrentDisposable()
    every { this@mockExecuteNullable.create(any()) } returns resultBlock()
}

@PublishedApi
internal fun <USE_CASE : CompletableUseCase<*>> USE_CASE.mockCurrentDisposable() {
    every { this@mockCurrentDisposable getProperty "currentDisposable" } returns null
    every { this@mockCurrentDisposable setProperty "currentDisposable" value any<Disposable>() } just runs
}
