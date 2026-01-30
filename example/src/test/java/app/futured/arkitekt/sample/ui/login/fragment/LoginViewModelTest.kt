package app.futured.arkitekt.sample.ui.login.fragment

import android.view.View
import app.futured.arkitekt.core.viewmodel.ViewModelTest
import app.futured.arkitekt.crusecases.test.mockExecute
import app.futured.arkitekt.sample.domain.GetStateUseCase
import app.futured.arkitekt.sample.domain.ObserveUserFullNameUseCase
import app.futured.arkitekt.sample.domain.SyncLoginUseCase
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
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
        viewModel = spyk(
            LoginViewModel(mockLoginCompletabler, mockObserveUserFullNameUseCase, mockGetStateUseCase, viewState),
            recordPrivateCalls = true
        )
    }

    @Test
    fun `when onStart is called and get state is successful then header is visible`() {
        // GIVEN
        mockObserveUserFullNameUseCase.mockExecute { emptyFlow() }
        mockGetStateUseCase.mockExecute(true) { flowOf(false) }

        // WHEN
        viewModel.onStart()

        // THEN
        assertEquals(View.VISIBLE, viewState.showHeader.value)
    }

    @Test
    fun `when onStart is called and get state is not successful then header is not visible`() {
        // GIVEN
        mockObserveUserFullNameUseCase.mockExecute { emptyFlow() }
        mockGetStateUseCase.mockExecute(true) { emptyFlow() }

        // WHEN
        viewModel.onStart()

        // THEN
        assertEquals(View.INVISIBLE, viewState.showHeader.value)
    }

    @Test
    fun `when onStart is called then full name is set to last observed value`() {
        // GIVEN
        mockGetStateUseCase.mockExecute(true) { emptyFlow() }
        mockObserveUserFullNameUseCase.mockExecute { flowOf("first", "second") }

        // WHEN
        viewModel.onStart()

        // THEN
        assertEquals("second", viewState.fullName.value)
    }

    @Test
    fun `when login is called then name and surname is send to interactor`() {
        // GIVEN
        mockGetStateUseCase.mockExecute(true) { emptyFlow() }
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
        verify { viewModel.sendEvent(NotifyActivityEvent("Successfully logged in!")) }
    }

    @Test
    fun `when login is called and use case is not successful then event is send`() {
        // GIVEN
        mockLoginCompletabler.mockExecute { throw IllegalStateException() }

        // WHEN
        viewModel.logIn()

        // THEN
        verify { viewModel.sendEvent(NotifyActivityEvent("Login error!")) }
    }

    @Test
    fun `when onBack is called then NavigateBackEvent is send`() {
        // WHEN
        viewModel.onBack()

        // THEN
        verify { viewModel.sendEvent(NavigateBackEvent) }
    }
}
