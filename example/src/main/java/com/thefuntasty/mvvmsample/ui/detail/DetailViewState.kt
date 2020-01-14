package com.thefuntasty.mvvmsample.ui.detail

import com.thefuntasty.mvvm.ViewState
import com.thefuntasty.mvvm.livedata.DefaultValueLiveData
import javax.inject.Inject

class DetailViewState @Inject constructor() : ViewState {

    val stringNumber = DefaultValueLiveData("")

    val number = DefaultValueLiveData(0)
}
