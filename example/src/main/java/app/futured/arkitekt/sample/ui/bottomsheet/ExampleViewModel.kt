package app.futured.arkitekt.sample.ui.bottomsheet

import app.futured.arkitekt.rxusecases.BaseRxViewModel
import javax.inject.Inject

class ExampleViewModel @Inject constructor(
    override val viewState: ExampleViewState
) : BaseRxViewModel<ExampleViewState>() {

    fun onClose() = sendEvent(CloseEvent)
}
