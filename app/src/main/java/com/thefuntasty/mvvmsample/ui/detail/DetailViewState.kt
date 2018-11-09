package com.thefuntasty.mvvmsample.ui.detail

import com.thefuntasty.mvvm.ViewState
import com.thefuntasty.mvvm.livedata.DefaultValueLiveData

class DetailViewState(stringNumber: String, number: Int) : ViewState {

    val stringNumber = DefaultValueLiveData(stringNumber)
    val number = DefaultValueLiveData(number)
}
