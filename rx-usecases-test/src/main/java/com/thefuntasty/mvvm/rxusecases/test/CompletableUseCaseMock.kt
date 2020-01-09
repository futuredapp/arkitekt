package com.thefuntasty.mvvm.rxusecases.test

import com.thefuntasty.mvvm.rxusecases.disposables.CompletableDisposablesOwner
import com.thefuntasty.mvvm.rxusecases.usecases.CompletableUseCase
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
 * mockCompletableUseCase.everyExecute(args = ...) { Completable.complete() }
 */
inline fun <reified ARGS : Any, USE_CASE : CompletableUseCase<ARGS>> USE_CASE.everyExecute(args: ARGS, resultBlock: () -> Completable) {
    mockCurrentDisposable()
    every { this@everyExecute.create(args) } returns resultBlock()
}

/**
 * Mock [CompletableDisposablesOwner.execute] method with `any()` matcher argument used as input argument.
 *
 * When the execute method will be called then the argument passed in [resultBlock] will be used as a result of mocked use case
 * and corresponding methods for the given use case will be called.
 * So when `Completable.complete` will be passed then `onComplete` will be called etc.
 *
 * Usage:
 * mockCompletableUseCase.everyExecute(args = ...) { Completable.complete() }
 */
inline fun <reified ARGS : Any, USE_CASE : CompletableUseCase<ARGS>> USE_CASE.everyExecute(resultBlock: () -> Completable) {
    mockCurrentDisposable()
    every { this@everyExecute.create(any()) } returns resultBlock()
}

/**
 * Mock [CompletableDisposablesOwner.execute] method for use cases with nullable input argument.
 *
 * When the execute method will be called then the argument passed in [resultBlock] will be used as a result of mocked use case
 * and corresponding methods for the given use case will be called.
 * So when `Completable.complete` will be passed then `onComplete` will be called etc.
 *
 * Usage:
 * mockCompletableUseCase.everyExecute(args = ...) { Completable.complete() }
 */
inline fun <reified ARGS : Any?, USE_CASE : CompletableUseCase<ARGS?>> USE_CASE.everyExecuteNullable(args: ARGS, resultBlock: () -> Completable) {
    mockCurrentDisposable()
    every { this@everyExecuteNullable.create(args) } returns resultBlock()
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
 * mockCompletableUseCase.everyExecute(args = ...) { Completable.complete() }
 */
inline fun <reified ARGS : Any, USE_CASE : CompletableUseCase<ARGS?>> USE_CASE.everyExecuteNullable(resultBlock: () -> Completable) {
    mockCurrentDisposable()
    every { this@everyExecuteNullable.create(any()) } returns resultBlock()
}

@PublishedApi
internal fun <USE_CASE : CompletableUseCase<*>> USE_CASE.mockCurrentDisposable() {
    every { this@mockCurrentDisposable getProperty "currentDisposable" } returns null
    every { this@mockCurrentDisposable setProperty "currentDisposable" value any<Disposable>() } just runs
}
