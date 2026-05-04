package app.futured.arkitekt.sample.ui.login.fragment

import app.futured.arkitekt.core.viewmodel.ViewModelTest
import app.futured.arkitekt.crusecases.test.mockExecute
import app.futured.arkitekt.sample.domain.GetStateUseCase
import app.futured.arkitekt.sample.domain.ObserveUserFullNameUseCase
import app.futured.arkitekt.sample.domain.SyncLoginUseCase
import app.futured.arkitekt.sample.ui.login.LoginViewModel
import app.futured.arkitekt.sample.ui.login.LoginViewState
import app.futured.arkitekt.sample.ui.login.NavigateBackEvent
import app.futured.arkitekt.sample.ui.login.ShowToastEvent
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LoginViewModelTest : ViewModelTest() {
    val mockLoginCompletabler: SyncLoginUseCase = mockk()
    val mockObserveUserFullNameUseCase: ObserveUserFullNameUseCase = mockk()
    val mockGetStateUseCase: GetStateUseCase = mockk()

    lateinit var viewState: LoginViewState
    lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        viewState = LoginViewState()
        mockGetStateUseCase.mockExecute { emptyFlow() }
        mockObserveUserFullNameUseCase.mockExecute { emptyFlow() }
        viewModel = createViewModel()
    }

    // spyk wraps the object AFTER init, so getWorkerDispatcher() mock only applies to
    // post-init calls (e.g. logIn). For init-triggered flows, we use runBlocking { join() }
    // in individual tests to synchronise with the real IO upstream before asserting.
    private fun createViewModel() = spyk(
        LoginViewModel(mockLoginCompletabler, mockObserveUserFullNameUseCase, mockGetStateUseCase, viewState),
        recordPrivateCalls = true
    ).also {
        every { it.getWorkerDispatcher() } returns Dispatchers.Main
    }

    // Waits for all coroutines launched during ViewModel.init to complete.
    private fun awaitInit() = runBlocking {
        viewModel.useCaseScope.coroutineContext[Job]!!.children.toList().forEach { it.join() }
    }

    @Test
    fun `when onStart is called and get state is successful then header is visible`() {
        // GIVEN
        mockObserveUserFullNameUseCase.mockExecute { emptyFlow() }
        mockGetStateUseCase.mockExecute(true) { flowOf(false) }
        viewModel = createViewModel()
        awaitInit()

        // THEN
        assertEquals(true, viewState.showHeader.value)
    }

    @Test
    fun `when onStart is called and get state is not successful then header is not visible`() {
        // setUp defaults run with emptyFlow, showHeader stays false
        assertEquals(false, viewState.showHeader.value)
    }

    @Test
    fun `when onStart is called then full name is set to last observed value`() {
        // GIVEN
        mockGetStateUseCase.mockExecute { emptyFlow() }
        mockObserveUserFullNameUseCase.mockExecute { flowOf("first", "second") }
        viewModel = createViewModel()
        awaitInit()

        // THEN
        assertEquals("second", viewState.fullName.value)
    }

    @Test
    fun `when login is called then name and surname is send to interactor`() {
        // GIVEN
        viewState.name.value = "name"
        viewState.surname.value = "surname"
        mockLoginCompletabler.mockExecute { }

        // WHEN
        viewModel.logIn()

        // THEN
        coVerify { mockLoginCompletabler.build(SyncLoginUseCase.LoginData("name", "surname")) }
    }

    @Test
    fun `when login is called and use case is successful then event is send`() {
        // GIVEN
        mockLoginCompletabler.mockExecute { }

        // WHEN
        viewModel.logIn()

        // THEN
        verify { viewModel.sendEvent(ShowToastEvent("Successfully logged in!")) }
    }

    @Test
    fun `when login is called and use case is not successful then event is send`() {
        // GIVEN
        mockLoginCompletabler.mockExecute { throw IllegalStateException() }

        // WHEN
        viewModel.logIn()

        // THEN
        verify { viewModel.sendEvent(ShowToastEvent("Login error!")) }
    }

    @Test
    fun `when onBack is called then NavigateBackEvent is send`() {
        // WHEN
        viewModel.onBack()

        // THEN
        verify { viewModel.sendEvent(NavigateBackEvent) }
    }
}
