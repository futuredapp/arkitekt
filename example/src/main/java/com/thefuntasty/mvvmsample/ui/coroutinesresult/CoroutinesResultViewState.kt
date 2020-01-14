package com.thefuntasty.mvvmsample.ui.coroutinesresult

import com.thefuntasty.mvvm.ViewState
import com.thefuntasty.mvvm.livedata.DefaultValueLiveData
import javax.inject.Inject

class CoroutinesResultViewState @Inject constructor() : ViewState {

    val contentState = DefaultValueLiveData(State.IDLE)

    val contentStateDescription = DefaultValueLiveData("")

    enum class State {
        IDLE, LOADING, RESULT, ERROR;

        fun isIdle() = this == IDLE

        fun isLoading() = this == LOADING

        fun isResult() = this == RESULT

        fun isError() = this == ERROR
    }
}
