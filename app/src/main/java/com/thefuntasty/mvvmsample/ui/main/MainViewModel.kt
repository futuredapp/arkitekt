package com.thefuntasty.mvvmsample.ui.main

import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvmsample.ui.ShowDetailEvent
import com.thefuntasty.mvvmsample.ui.ShowFormEvent
import javax.inject.Inject

class MainViewModel @Inject constructor() : BaseViewModel() {

    override val clickDebounce: Long
        get() = 500L

    fun onDetail() {
        onDebounce {
            sendEvent(ShowDetailEvent)
        }
    }

    fun onForm() {
        onDebounce {
            sendEvent(ShowFormEvent)
        }
    }
}
