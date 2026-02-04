package app.futured.arkitekt.sample.ui.bottomsheet

import app.futured.arkitekt.crusecases.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExampleViewModel @Inject constructor(
    override val viewState: ExampleViewState
) : BaseViewModel<ExampleViewState>() {

    fun onClose() = sendEvent(CloseEvent)
}
