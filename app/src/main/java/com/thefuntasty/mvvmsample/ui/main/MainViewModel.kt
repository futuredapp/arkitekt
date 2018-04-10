package com.thefuntasty.mvvmsample.ui.main

import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvmsample.ui.ShowDetailEvent
import javax.inject.Inject

class MainViewModel @Inject constructor() : BaseViewModel() {

    fun onDetail() {
        sendEvent(ShowDetailEvent)
    }
}
