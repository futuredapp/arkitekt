package app.futured.arkitekt.sample.ui.bottomsheet

import app.futured.arkitekt.core.viewmodel.ViewModelTest
import io.mockk.spyk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class ExampleViewModelTest : ViewModelTest() {

    lateinit var viewState: ExampleViewState
    lateinit var viewModel: ExampleViewModel

    @Before
    fun setUp() {
        viewState = ExampleViewState()
        viewModel = spyk(ExampleViewModel(viewState), recordPrivateCalls = true)
    }

    @Test
    fun `when onClose is called then close event is send`() {
        // WHEN
        viewModel.onClose()

        // THEN
        verify { viewModel.sendEvent(CloseEvent) }
    }
}
