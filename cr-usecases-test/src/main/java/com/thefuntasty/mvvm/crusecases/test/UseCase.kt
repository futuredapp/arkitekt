package com.thefuntasty.mvvm.crusecases.test

import com.thefuntasty.mvvm.crusecases.CoroutineScopeOwner
import com.thefuntasty.mvvm.crusecases.UseCase
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
 * mockUseCase.everyExecute(args = ...) { ... }
 */
fun <ARGS, RETURN_VALUE, USE_CASE : UseCase<ARGS, RETURN_VALUE>> USE_CASE.everyExecute(args: ARGS, returnBlock: () -> RETURN_VALUE) {
    mockDeferred()
    coEvery { this@everyExecute.build(args) } answers { returnBlock() }
}

/**
 * Mock [CoroutineScopeOwner.execute] method with `any()` matcher argument used as input argument.
 *
 * When the execute method will be called then the argument passed in [returnBlock] will be used as a result of mocked use case
 * and corresponding methods for the given use case will be called.
 *
 * Usage:
 * mockUseCase.everyExecute { ... }
 */
inline fun <reified ARGS : Any, RETURN_VALUE, USE_CASE : UseCase<ARGS, RETURN_VALUE>> USE_CASE.everyExecute(crossinline returnBlock: () -> RETURN_VALUE) {
    mockDeferred()
    coEvery { this@everyExecute.build(any()) } answers { returnBlock() }
}

/**
 * Mock [CoroutineScopeOwner.execute] method for use cases with nullable input argument.
 *
 * When the execute method will be called then the argument passed in [returnBlock] will be used as a result of mocked use case
 * and corresponding methods for the given use case will be called.
 *
 * Usage:
 * mockUseCase.everyExecute(args = ...) { ... }
 */
inline fun <reified ARGS : Any, RETURN_VALUE, USE_CASE : UseCase<ARGS?, RETURN_VALUE>> USE_CASE.everyExecuteNullable(args: ARGS?, crossinline returnBlock: () -> RETURN_VALUE) {
    mockDeferred()
    coEvery { this@everyExecuteNullable.build(args) } answers { returnBlock() }
}

/**
 * Mock [CoroutineScopeOwner.execute] method for use cases with nullable input argument
 * and `any()` matcher argument used as input argument.
 *
 * When the execute method will be called then the argument passed in [returnBlock] will be used as a result of mocked use case
 * and corresponding methods for the given use case will be called.
 *
 * Usage:
 * mockUseCase.everyExecute { ... }
 */
inline fun <reified ARGS : Any, RETURN_VALUE, USE_CASE : UseCase<ARGS?, RETURN_VALUE>> USE_CASE.everyExecuteNullable(crossinline returnBlock: () -> RETURN_VALUE) {
    mockDeferred()
    coEvery { this@everyExecuteNullable.build(any()) } answers { returnBlock() }
}

@PublishedApi
internal fun <ARGS, RETURN_VALUE, USE_CASE : UseCase<ARGS, RETURN_VALUE>> USE_CASE.mockDeferred() {
    coEvery { this@mockDeferred.deferred = any() } just Runs
    coEvery { this@mockDeferred.deferred } returns mockk(relaxUnitFun = true)
}
