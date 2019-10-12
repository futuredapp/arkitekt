package com.thefuntasty.mvvm.viewmodel.testactivity

import androidx.lifecycle.MutableLiveData
import com.thefuntasty.mvvm.ViewState
import com.thefuntasty.mvvm.livedata.DefaultValueLiveData
import com.thefuntasty.mvvm.livedata.DefaultValueMediatorLiveData

class TestViewState : ViewState {
    var onStartCallCount = 0
    var onClearedCallback: (() -> Unit)? = null
    val testIntegerLiveData = MutableLiveData<Int>()
    val testIntegerDefaultLiveData = DefaultValueLiveData(1)
    val testIntegerMediatorLiveData = DefaultValueMediatorLiveData(1)
}
