package com.thefuntasty.mvvmsample.ui.form

import com.thefuntasty.mvvm.BaseViewModel
import javax.inject.Inject

class FormViewModel @Inject constructor() : BaseViewModel<FormViewState>() {

    override val viewState = FormViewState("", "")

    fun onSubmit() {
        sendEvent(ShowToastEvent("Submit"))
    }
}
