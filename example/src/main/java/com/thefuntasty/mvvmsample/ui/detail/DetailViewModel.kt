package com.thefuntasty.mvvmsample.ui.detail

import com.thefuntasty.mvvm.BaseViewModel
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    override val viewState: DetailViewState
) : BaseViewModel<DetailViewState>() {

    override fun onStart() {
        viewState.number.observeWithoutOwner { viewState.stringNumber.value = it.toString() }
    }

    fun incrementNumber() {
        viewState.number.value = viewState.number.value + 1
    }

    fun onBack() {
        sendEvent(NavigateBackEvent)
    }
}
