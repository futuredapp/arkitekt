package com.thefuntasty.mvvmsample.ui.form

import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.livedata.DefaultValueLiveData
import com.thefuntasty.mvvm.livedata.liveDataCombineTwo
import com.thefuntasty.mvvmsample.ui.ShowToastEvent
import javax.inject.Inject

class FormViewModel @Inject constructor() : BaseViewModel() {

    val login = DefaultValueLiveData("")
    val password = DefaultValueLiveData("")

    val submitEnabled = liveDataCombineTwo(login, password) {
        login, password -> login.isNotEmpty() && password.isNotEmpty()
    }

    fun onSubmit() {
        sendEvent(ShowToastEvent("Submit"))
    }
}
