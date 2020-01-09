package com.thefuntasty.mvvm.test

import androidx.lifecycle.MutableLiveData
import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.ViewState
import com.thefuntasty.mvvm.livedata.DefaultValueLiveData
import com.thefuntasty.mvvm.livedata.DefaultValueMediatorLiveData
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class BaseViewModelExtensionsMockTest : ViewModelTest() {

    class TestViewState : ViewState {
        val mutableLiveData = MutableLiveData<Int>()
        val defaultValueLiveData = DefaultValueLiveData(0)
        val defaultValueMediatorLiveData = DefaultValueMediatorLiveData(0)
    }

    class TestViewModel(override val viewState: TestViewState) : BaseViewModel<TestViewState>()

    lateinit var viewModel: TestViewModel

    @Before
    fun setUp() {
        viewModel = spyk(TestViewModel(mockk()), recordPrivateCalls = true)
    }

    @Test
    fun `everyObserveWithoutOwner should call captured lambda`() = with(viewModel) {
        // GIVEN
        val capturedLambda = viewModel.mockObserveWithoutOwner { viewModel.viewState.mutableLiveData }

        // WHEN
        var result = 0
        viewModel.viewState.mutableLiveData.observeWithoutOwner { result = it }
        capturedLambda(10)
        capturedLambda(100)

        // THEN
        assertEquals(100, result)
    }

    @Test
    fun `everyObserveWithoutOwnerDefaultValue should call captured lambda`() = with(viewModel) {
        // GIVEN
        val capturedLambda = mockObserveWithoutOwnerDefaultValue { viewModel.viewState.defaultValueLiveData }

        // WHEN
        var result = 0
        viewModel.viewState.defaultValueLiveData.observeWithoutOwner { result = it }
        capturedLambda(10)
        capturedLambda(100)

        // THEN
        assertEquals(100, result)
    }

    @Test
    fun `everyObserveWithoutOwnerDefaultValueMediator should call captured lambda`() = with(viewModel) {
        // GIVEN
        val capturedLambda = mockObserveWithoutOwnerDefaultValueMediator { viewModel.viewState.defaultValueMediatorLiveData }

        // WHEN
        var result = 0
        viewModel.viewState.defaultValueMediatorLiveData.observeWithoutOwner { result = it }
        capturedLambda(10)
        capturedLambda(100)

        // THEN
        assertEquals(100, result)
    }
}
