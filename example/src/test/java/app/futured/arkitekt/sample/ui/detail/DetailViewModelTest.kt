package app.futured.arkitekt.sample.ui.detail

import app.futured.arkitekt.core.viewmodel.ViewModelTest
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

}
