package app.futured.arkitekt.core.testactivity

import androidx.lifecycle.MutableLiveData
import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.core.livedata.DefaultValueLiveData
import app.futured.arkitekt.core.livedata.DefaultValueMediatorLiveData

class TestViewState : ViewState {
    var onStartCallCount = 0
    var onClearedCallback: (() -> Unit)? = null
    val testIntegerLiveData = MutableLiveData<Int>()
    val testIntegerDefaultLiveData = DefaultValueLiveData(1)
    val testIntegerMediatorLiveData = DefaultValueMediatorLiveData(1)
}
