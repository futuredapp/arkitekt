package com.thefuntasty.mvvm.test

import androidx.lifecycle.LiveData
import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.livedata.DefaultValueLiveData
import com.thefuntasty.mvvm.livedata.DefaultValueMediatorLiveData
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
 * val capturedObserveLambda = viewModel.everyObserveWithoutOwner { viewState.number }
 *
 * // WHEN
 * capturedObserveLambda.invoke(...)
 *
 */
fun <VALUE> BaseViewModel<*>.everyObserveWithoutOwner(liveData: () -> LiveData<VALUE>): (VALUE) -> Unit {
    var invokable: (CapturingSlot<(VALUE) -> Unit>)? = null
    every { liveData().observeWithoutOwner(captureLambda()) } answers {
        invokable = lambda()
    }
    return { value -> invokable?.invoke(value) ?: error("Lambda wasn't captured yet") }
}

/**
 * Extension function that helps with mocking of [BaseViewModel.observeWithoutOwner]
 *
 * @return Captured lambda that is passed to original method. [invoke] should be called in order to simulate view state change.
 *
 * Usage:
 *
 * // GIVEN
 * val capturedObserveLambda = viewModel.everyObserveWithoutOwner { viewState.number }
 *
 * // WHEN
 * capturedObserveLambda.invoke(...)
 *
 */
fun <VALUE : Any> BaseViewModel<*>.everyObserveWithoutOwnerDefaultValue(liveData: () -> DefaultValueLiveData<VALUE>): (VALUE) -> Unit {
    var invokable: (CapturingSlot<(VALUE) -> Unit>)? = null
    every { liveData().observeWithoutOwner(captureLambda()) } answers {
        invokable = lambda()
    }
    return { value -> invokable?.invoke(value) ?: error("Lambda wasn't captured yet") }
}

/**
 * Extension function that helps with mocking of [BaseViewModel.observeWithoutOwner]
 *
 * @return Captured lambda that is passed to original method. [invoke] should be called in order to simulate view state change.
 *
 * Usage:
 *
 * // GIVEN
 * val capturedObserveLambda = viewModel.everyObserveWithoutOwner { viewState.number }
 *
 * // WHEN
 * capturedObserveLambda.invoke(...)
 *
 */
fun <VALUE : Any> BaseViewModel<*>.everyObserveWithoutOwnerDefaultValueMediator(liveData: () -> DefaultValueMediatorLiveData<VALUE>): (VALUE) -> Unit {
    var invokable: (CapturingSlot<(VALUE) -> Unit>)? = null
    every { liveData().observeWithoutOwner(captureLambda()) } answers {
        invokable = lambda()
    }
    return { value -> invokable?.invoke(value) ?: error("Lambda wasn't captured yet") }
}
