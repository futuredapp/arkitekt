package com.thefuntasty.mvvmsample.ui.detail

import com.thefuntasty.mvvm.BaseViewModel
import javax.inject.Inject

class DetailViewModel @Inject constructor() : BaseViewModel<DetailViewState>() {

    override val viewState = DetailViewState("", 0)

    override fun onStart() {
        viewState.number.observeWithoutOwner { viewState.stringNumber.value = it.toString() }
    }

    fun incrementNumber() {
        viewState.number.value = viewState.number.value + 1
    }
}
