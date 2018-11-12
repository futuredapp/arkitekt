package com.thefuntasty.mvvmsample.ui.main

import com.thefuntasty.mvvm.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor() : BaseViewModel<MainViewState>() {

    override val viewState = MainViewState

    fun onDetail() {
        sendEvent(ShowDetailEvent)
    }

    fun onForm() {
        sendEvent(ShowFormEvent)
    }

    fun onLogin() {
        sendEvent(ShowLoginEvent)
    }
}
