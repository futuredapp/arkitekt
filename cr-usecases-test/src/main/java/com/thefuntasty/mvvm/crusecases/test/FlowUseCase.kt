package com.thefuntasty.mvvm.crusecases.test

import com.thefuntasty.mvvm.crusecases.CoroutineScopeOwner
import com.thefuntasty.mvvm.crusecases.FlowUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow

/**
 * Mock [CoroutineScopeOwner.execute] method.
 *
 * When the execute method will be called then the argument passed in [returnBlock] will be used as a result of mocked use case
 * and corresponding methods for the given use case will be called.
 *
 * Usage:
 * mockUseCase.everyExecute(args = ...) { ... }
 */
fun <ARGS, RETURN_VALUE, USE_CASE : FlowUseCase<ARGS, RETURN_VALUE>> USE_CASE.everyExecute(args: ARGS, returnBlock: () -> Flow<RETURN_VALUE>) {
    mockJob()
    coEvery { this@everyExecute.build(args) } returns returnBlock()
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
inline fun <reified ARGS : Any, RETURN_VALUE, USE_CASE : FlowUseCase<ARGS, RETURN_VALUE>> USE_CASE.everyExecute(returnBlock: () -> Flow<RETURN_VALUE>) {
    mockJob()
    coEvery { this@everyExecute.build(any()) } returns returnBlock()
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
inline fun <reified ARGS : Any, RETURN_VALUE, USE_CASE : FlowUseCase<ARGS?, RETURN_VALUE>> USE_CASE.everyExecuteNullable(args: ARGS?, returnBlock: () -> Flow<RETURN_VALUE>) {
    mockJob()
    coEvery { this@everyExecuteNullable.build(args) } returns returnBlock()
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
inline fun <reified ARGS : Any, RETURN_VALUE, USE_CASE : FlowUseCase<ARGS?, RETURN_VALUE>> USE_CASE.everyExecuteNullable(returnBlock: () -> Flow<RETURN_VALUE>) {
    mockJob()
    coEvery { this@everyExecuteNullable.build(any()) } returns returnBlock()
}

@PublishedApi
internal fun <ARGS, RETURN_VALUE, USE_CASE : FlowUseCase<ARGS, RETURN_VALUE>> USE_CASE.mockJob() {
    coEvery { this@mockJob.job = any() } just Runs
    coEvery { this@mockJob.job } returns mockk(relaxUnitFun = true)
}
