package app.futured.arkitekt.sample.ui.form

import app.futured.arkitekt.core.viewmodel.ViewModelTest
import app.futured.arkitekt.crusecases.test.mockExecute
import app.futured.arkitekt.sample.domain.ObserveFormUseCase
import app.futured.arkitekt.sample.domain.SaveFormUseCase
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FormViewModelTest : ViewModelTest() {

    val mockSaveFormUseCase: SaveFormUseCase = mockk()
    val mockObserveFormUseCase: ObserveFormUseCase = mockk()

    lateinit var viewState: FormViewState
    lateinit var viewModel: FormViewModel

    @Before
    fun setUp() {
        viewState = FormViewState()
        viewModel = spyk(FormViewModel(mockSaveFormUseCase, mockObserveFormUseCase, viewState), recordPrivateCalls = true)
    }

    @Test
    fun `when onSubmit is called then form is saved and ShowToastEvent is send`() {
        // GIVEN
        val inputArgs = SaveFormUseCase.Data(viewState.login.value to viewState.password.value)
        mockSaveFormUseCase.mockExecute(inputArgs) { "A" to "B" }

        // WHEN
        viewModel.onSubmit()

        // THEN
        verify { viewModel.sendEvent(ShowToastEvent("A B")) }
    }

    @Test
    fun `when onStart is called then form is observed and most actual value is set to storedContent`() {
        // GIVEN
        mockObserveFormUseCase.mockExecute(Unit) { flowOf("A" to "B", "B" to "C") }

        // WHEN
        viewModel.onStart()

        // THEN
        assertEquals("B C", viewState.storedContent.value)
    }

    @Test
    fun `when onStart is called then form is observed and when error occurs then ShowToastEvent is send`() {
        // GIVEN
        mockObserveFormUseCase.mockExecute(Unit) { flow { throw IllegalStateException() } }

        // WHEN
        viewModel.onStart()

        // THEN
        verify { viewModel.sendEvent(ShowToastEvent("Error :-(")) }
    }
}
