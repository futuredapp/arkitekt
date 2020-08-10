package app.futured.arkitekt.sample.ui.coroutinesresult

import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.core.livedata.DefaultValueLiveData
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
