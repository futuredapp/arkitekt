package app.futured.arkitekt.sample.ui.form

import app.futured.arkitekt.core.viewmodel.ViewModelTest
import app.futured.arkitekt.crusecases.test.mockExecute
import app.futured.arkitekt.sample.domain.ObserveFormUseCase
import app.futured.arkitekt.sample.domain.SaveFormUseCase
import app.futured.arkitekt.sample.ui.compose.ExampleRoute
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
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
        mockObserveFormUseCase.mockExecute { emptyFlow() }
        viewModel = createViewModel()
    }

    private fun createViewModel() = spyk(
        FormViewModel(mockSaveFormUseCase, mockObserveFormUseCase, viewState, route = ExampleRoute.Form("form")),
        recordPrivateCalls = true
    ).also {
        every { it.getWorkerDispatcher() } returns Dispatchers.Main
    }

    private fun awaitInit() = runBlocking {
        viewModel.coroutineScope.coroutineContext[Job]!!.children.toList().forEach { it.join() }
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
        viewModel = createViewModel()
        awaitInit()

        // THEN
        assertEquals("B C", viewState.storedContent.value)
    }

    @Test
    fun `when onStart is called then form is observed and when error occurs then ShowToastEvent is send`() {
        // GIVEN
        mockObserveFormUseCase.mockExecute(Unit) { flow { throw IllegalStateException() } }
        viewModel = createViewModel()
        awaitInit()

        // THEN
        val receivedEvent = runBlocking { viewModel.events.first() }
        assertEquals(ShowToastEvent("Error :-("), receivedEvent)
    }
}
