package app.futured.arkitekt.sample.ui.login

import androidx.compose.runtime.mutableStateOf
import app.futured.arkitekt.core.ViewState
import javax.inject.Inject

class LoginViewState @Inject constructor() : ViewState {
    val name = mutableStateOf("")
    val surname = mutableStateOf("")

    val fullName = mutableStateOf("")
    val showHeader = mutableStateOf(false)
}
