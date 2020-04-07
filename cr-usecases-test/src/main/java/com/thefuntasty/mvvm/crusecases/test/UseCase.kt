package com.thefuntasty.mvvm.crusecases.test

import app.futured.arkitekt.crusecases.CoroutineScopeOwner
import app.futured.arkitekt.crusecases.UseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk

/**
 * Mock [CoroutineScopeOwner.execute] method.
 *
 * When the execute method will be called then the argument passed in [returnBlock] will be used as a result of mocked use case
 * and corresponding methods for the given use case will be called.
 *
 * Usage:
 * mockUseCase.mockExecute(args = ...) { ... }
 */
fun <ARGS, RETURN_VALUE, USE_CASE : UseCase<ARGS, RETURN_VALUE>> USE_CASE.mockExecute(args: ARGS, returnBlock: () -> RETURN_VALUE) {
    mockDeferred()
    coEvery { this@mockExecute.build(args) } answers { returnBlock() }
}

/**
 * Mock [CoroutineScopeOwner.execute] method with `any()` matcher argument used as input argument.
 *
 * When the execute method will be called then the argument passed in [returnBlock] will be used as a result of mocked use case
 * and corresponding methods for the given use case will be called.
 *
 * Usage:
 * mockUseCase.mockExecute { ... }
 */
inline fun <reified ARGS : Any, RETURN_VALUE, USE_CASE : UseCase<ARGS, RETURN_VALUE>> USE_CASE.mockExecute(crossinline returnBlock: () -> RETURN_VALUE) {
    mockDeferred()
    coEvery { this@mockExecute.build(any()) } answers { returnBlock() }
}

/**
 * Mock [CoroutineScopeOwner.execute] method for use cases with nullable input argument.
 *
 * When the execute method will be called then the argument passed in [returnBlock] will be used as a result of mocked use case
 * and corresponding methods for the given use case will be called.
 *
 * Usage:
 * mockUseCase.mockExecute(args = ...) { ... }
 */
inline fun <reified ARGS : Any, RETURN_VALUE, USE_CASE : UseCase<ARGS?, RETURN_VALUE>> USE_CASE.mockExecuteNullable(args: ARGS?, crossinline returnBlock: () -> RETURN_VALUE) {
    mockDeferred()
    coEvery { this@mockExecuteNullable.build(args) } answers { returnBlock() }
}

/**
 * Mock [CoroutineScopeOwner.execute] method for use cases with nullable input argument
 * and `any()` matcher argument used as input argument.
 *
 * When the execute method will be called then the argument passed in [returnBlock] will be used as a result of mocked use case
 * and corresponding methods for the given use case will be called.
 *
 * Usage:
 * mockUseCase.mockExecute { ... }
 */
inline fun <reified ARGS : Any, RETURN_VALUE, USE_CASE : UseCase<ARGS?, RETURN_VALUE>> USE_CASE.mockExecuteNullable(crossinline returnBlock: () -> RETURN_VALUE) {
    mockDeferred()
    coEvery { this@mockExecuteNullable.build(any()) } answers { returnBlock() }
}

@PublishedApi
internal fun <ARGS, RETURN_VALUE, USE_CASE : UseCase<ARGS, RETURN_VALUE>> USE_CASE.mockDeferred() {
    coEvery { this@mockDeferred.deferred = any() } just Runs
    coEvery { this@mockDeferred.deferred } returns mockk(relaxUnitFun = true)
}
