package app.futured.arkitekt.core.testactivity

import app.futured.arkitekt.core.BaseViewModel

open class TestViewModel(
    override val viewState: TestViewState
) : BaseViewModel<TestViewState>() {

    override fun onStart() {
        viewState.onStartCallCount++
    }

    fun sentEmptyEvent() {
        sendEvent(EmptyEvent)
    }

    fun observerTestIntegerLiveData(callback: (Int) -> Unit) {
        viewState.testIntegerLiveData.observeWithoutOwner(callback)
    }

    fun observeTestIntegerDefaultLiveData(callback: (Int) -> Unit) {
        viewState.testIntegerDefaultLiveData.observeWithoutOwner(callback)
    }

    fun observeTestDefaultMediatorLiveData(callback: (Int) -> Unit) {
        viewState.testIntegerMediatorLiveData.observeWithoutOwner(callback)
    }

    override fun onCleared() {
        viewState.onClearedCallback?.invoke()
        super.onCleared()
    }
}
