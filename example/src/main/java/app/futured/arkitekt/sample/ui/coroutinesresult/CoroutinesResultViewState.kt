package app.futured.arkitekt.sample.ui.coroutinesresult

import androidx.lifecycle.MutableLiveData
import app.futured.arkitekt.core.ViewState
import javax.inject.Inject

class CoroutinesResultViewState @Inject constructor() : ViewState {

    val contentState = MutableLiveData(State.IDLE)

    val contentStateDescription = MutableLiveData("")

    enum class State {
        IDLE, LOADING, RESULT, ERROR;

        fun isIdle() = this == IDLE

        fun isLoading() = this == LOADING

        fun isResult() = this == RESULT

        fun isError() = this == ERROR
    }
}
