package app.futured.arkitekt.sample.ui.login.activity

import app.futured.arkitekt.core.viewmodel.ViewModelTest
import io.mockk.spyk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class LoginViewModelTest : ViewModelTest() {

    lateinit var viewState: LoginViewState
    lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        viewState = LoginViewState()
        viewModel = spyk(LoginViewModel(viewState), recordPrivateCalls = true)
    }

    @Test
    fun `when sendToastEvent is called then message event is send`() {
        // WHEN
        viewModel.sendToastEvent("MESSAGE")

        // THEN
        verify { viewModel.sendEvent(ShowToastEvent("LoginActivity test toast: MESSAGE")) }
    }
}
