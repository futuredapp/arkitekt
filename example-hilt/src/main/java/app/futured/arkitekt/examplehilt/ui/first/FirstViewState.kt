package app.futured.arkitekt.examplehilt.ui.first

import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.core.livedata.UiData
import javax.inject.Inject

class FirstViewState @Inject constructor() : ViewState {
    val randomNumber = UiData(0)
}
