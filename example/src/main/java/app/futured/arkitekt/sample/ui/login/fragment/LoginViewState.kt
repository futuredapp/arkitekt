package app.futured.arkitekt.sample.ui.login.fragment

import android.view.View
import androidx.lifecycle.MutableLiveData
import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.core.livedata.UiData
import javax.inject.Inject

class LoginViewState @Inject constructor() : ViewState {
    val name = UiData("")
    val surname = UiData("")

    val fullName = MutableLiveData<String>()
    val showHeader = MutableLiveData(View.INVISIBLE)
}
