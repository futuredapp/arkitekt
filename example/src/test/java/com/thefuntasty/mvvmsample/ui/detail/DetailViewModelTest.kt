package com.thefuntasty.mvvmsample.ui.detail

import com.thefuntasty.mvvm.test.viewmodel.ViewModelTest
import com.thefuntasty.mvvm.test.viewmodel.mockObserveWithoutOwnerDefaultValue
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Before
import org.junit.Test

class DetailViewModelTest : ViewModelTest() {

    lateinit var viewState: DetailViewState
    lateinit var viewModel: DetailViewModel

    @Before
    fun setUp() {
        viewState = mockk(relaxed = true)
        viewModel = spyk(DetailViewModel(viewState), recordPrivateCalls = true)
    }

    @Test
    fun `when incrementNumber is called then number value is incremented by one`() {
        // GIVEN
        every { viewState.number.value } returns 10

        // WHEN
        viewModel.incrementNumber()

        // THEN
        verify { viewState.number.value = 11 }
    }

    @Test
    fun `when onBack is called then NavigateBackEvent is sent`() {
        // WHEN
        viewModel.onBack()

        // THEN
        verify { viewModel.sendEvent(NavigateBackEvent) }
    }

    @Test
    fun `when onStart is called then number value is observed and string number is updated`() {
        // GIVEN
        val observeNumberLambda = viewModel.mockObserveWithoutOwnerDefaultValue { viewState.number }

        // WHEN
        viewModel.onStart()
        observeNumberLambda.invoke(10)
        observeNumberLambda.invoke(100)

        // THEN
        verifyOrder {
            viewState.stringNumber.value = "10"
            viewState.stringNumber.value = "100"
        }
    }
}
