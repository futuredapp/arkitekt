package app.futured.arkitekt.sample.ui.form

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import app.futured.arkitekt.core.ViewState
import javax.inject.Inject

class FormViewState @Inject constructor() : ViewState {

    val login = mutableStateOf("")
    val password = mutableStateOf("")
    val submitEnabled = derivedStateOf {
        login.value.isNotEmpty() && password.value.isNotEmpty()
    }
    val storedContent = mutableStateOf("")
}
