package app.futured.arkitekt.examplehilt.ui

import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.core.livedata.DefaultValueLiveData
import javax.inject.Inject

class MainViewState @Inject constructor() : ViewState {

    val number = DefaultValueLiveData("0")
}
