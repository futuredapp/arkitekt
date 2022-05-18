package app.futured.arkitekt.core.viewmodel

import androidx.lifecycle.LiveData
import app.futured.arkitekt.core.BaseViewModel
import app.futured.arkitekt.core.livedata.DefaultValueLiveData
import app.futured.arkitekt.core.livedata.DefaultValueMediatorLiveData
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.invoke

/**
 * Extension function that helps with mocking of [BaseViewModel.observeWithoutOwner]
 *
 * @return Captured lambda that is passed to original method. [invoke] should be called in order to simulate view state change.
 *
 * Usage:
 *
 * // GIVEN
 * val capturedObserveLambda = viewModel.mockObserveWithoutOwner { viewState.number }
 *
 * // WHEN
 * capturedObserveLambda.invoke(...)
 *
 */
fun <VALUE> BaseViewModel<*>.mockObserveWithoutOwner(liveData: () -> LiveData<VALUE>): (VALUE) -> Unit {
    var invokable: (CapturingSlot<(VALUE) -> Unit>)? = null
    every { liveData().observeWithoutOwner(captureLambda()) } answers {
        invokable = lambda()
    }
    return { value -> invokable?.invoke(value) ?: lambdaNotCapturedError() }
}

/**
 * Extension function that helps with mocking of [BaseViewModel.observeWithoutOwner]
 *
 * @return Captured lambda that is passed to original method. [invoke] should be called in order to simulate view state change.
 *
 * Usage:
 *
 * // GIVEN
 * val capturedObserveLambda = viewModel.mockObserveWithoutOwner { viewState.number }
 *
 * // WHEN
 * capturedObserveLambda.invoke(...)
 *
 */
fun <VALUE : Any> BaseViewModel<*>.mockObserveWithoutOwnerDefaultValue(liveData: () -> DefaultValueLiveData<VALUE>): (VALUE) -> Unit {
    var invokable: (CapturingSlot<(VALUE) -> Unit>)? = null
    every { liveData().observeWithoutOwner(captureLambda()) } answers {
        invokable = lambda()
    }
    return { value -> invokable?.invoke(value) ?: lambdaNotCapturedError() }
}

/**
 * Extension function that helps with mocking of [BaseViewModel.observeWithoutOwner]
 *
 * @return Captured lambda that is passed to original method. [invoke] should be called in order to simulate view state change.
 *
 * Usage:
 *
 * // GIVEN
 * val capturedObserveLambda = viewModel.mockObserveWithoutOwner { viewState.number }
 *
 * // WHEN
 * capturedObserveLambda.invoke(...)
 *
 */
@Suppress("FunctionMaxLength")
fun <VALUE : Any> BaseViewModel<*>.mockObserveWithoutOwnerDefaultValueMediator(liveData: () -> DefaultValueMediatorLiveData<VALUE>): (VALUE) -> Unit {
    var invokable: (CapturingSlot<(VALUE) -> Unit>)? = null
    every { liveData().observeWithoutOwner(captureLambda()) } answers {
        invokable = lambda()
    }
    return { value -> invokable?.invoke(value) ?: lambdaNotCapturedError() }
}

private fun lambdaNotCapturedError(): Nothing = error("Lambda wasn't captured yet")
