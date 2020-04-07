package app.futured.arkitekt.crusecases.test

import app.futured.arkitekt.crusecases.CoroutineScopeOwner
import app.futured.arkitekt.crusecases.FlowUseCase
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
 * mockUseCase.mockExecute(args = ...) { ... }
 */
fun <ARGS, RETURN_VALUE, USE_CASE : FlowUseCase<ARGS, RETURN_VALUE>> USE_CASE.mockExecute(args: ARGS, returnBlock: () -> Flow<RETURN_VALUE>) {
    mockJob()
    coEvery { this@mockExecute.build(args) } returns returnBlock()
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
inline fun <reified ARGS : Any, RETURN_VALUE, USE_CASE : FlowUseCase<ARGS, RETURN_VALUE>> USE_CASE.mockExecute(returnBlock: () -> Flow<RETURN_VALUE>) {
    mockJob()
    coEvery { this@mockExecute.build(any()) } returns returnBlock()
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
inline fun <reified ARGS : Any, RETURN_VALUE, USE_CASE : FlowUseCase<ARGS?, RETURN_VALUE>> USE_CASE.mockExecuteNullable(args: ARGS?, returnBlock: () -> Flow<RETURN_VALUE>) {
    mockJob()
    coEvery { this@mockExecuteNullable.build(args) } returns returnBlock()
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
inline fun <reified ARGS : Any, RETURN_VALUE, USE_CASE : FlowUseCase<ARGS?, RETURN_VALUE>> USE_CASE.mockExecuteNullable(returnBlock: () -> Flow<RETURN_VALUE>) {
    mockJob()
    coEvery { this@mockExecuteNullable.build(any()) } returns returnBlock()
}

@PublishedApi
internal fun <ARGS, RETURN_VALUE, USE_CASE : FlowUseCase<ARGS, RETURN_VALUE>> USE_CASE.mockJob() {
    coEvery { this@mockJob.job = any() } just Runs
    coEvery { this@mockJob.job } returns mockk(relaxUnitFun = true)
}
