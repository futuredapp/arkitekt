package app.futured.arkitekt.sample.ui.bottomsheet

import app.futured.arkitekt.crusecases.BaseCrViewModel
import javax.inject.Inject

class ExampleViewModel @Inject constructor(
    override val viewState: ExampleViewState
) : BaseCrViewModel<ExampleViewState>() {

    fun onClose() = sendEvent(CloseEvent)
}
