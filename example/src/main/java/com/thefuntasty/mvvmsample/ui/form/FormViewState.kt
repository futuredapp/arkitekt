package com.thefuntasty.mvvmsample.ui.form

import com.thefuntasty.mvvm.ViewState
import com.thefuntasty.mvvm.livedata.DefaultValueLiveData
import com.thefuntasty.mvvm.livedata.combineLiveData
import javax.inject.Inject

class FormViewState @Inject constructor() : ViewState {

    val login = DefaultValueLiveData("")
    val password = DefaultValueLiveData("")
    val submitEnabled = combineLiveData(this.login, this.password) { login, password ->
        login.isNotEmpty() && password.isNotEmpty()
    }
    val storedContent = DefaultValueLiveData("")
}
