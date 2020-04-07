package com.thefuntasty.mvvmsample.ui.form

import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.core.livedata.DefaultValueLiveData
import app.futured.arkitekt.core.livedata.combineLiveData
import javax.inject.Inject

class FormViewState @Inject constructor() : ViewState {

    val login = DefaultValueLiveData("")
    val password = DefaultValueLiveData("")
    val submitEnabled = combineLiveData(this.login, this.password) { login, password ->
        login.isNotEmpty() && password.isNotEmpty()
    }
    val storedContent = DefaultValueLiveData("")
}
