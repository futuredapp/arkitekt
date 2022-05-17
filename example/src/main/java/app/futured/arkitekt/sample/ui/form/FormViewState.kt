package app.futured.arkitekt.sample.ui.form

import androidx.lifecycle.MutableLiveData
import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.core.livedata.UiData
import app.futured.arkitekt.core.livedata.combineLiveData
import app.futured.arkitekt.core.livedata.uiData
import javax.inject.Inject

class FormViewState @Inject constructor() : ViewState {

    val login = uiData { "" }
    val password = uiData { "" }
    val submitEnabled = combineLiveData(this.login, this.password) { login, password ->
        login.isNotEmpty() && password.isNotEmpty()
    }
    val storedContent = UiData("")
}
