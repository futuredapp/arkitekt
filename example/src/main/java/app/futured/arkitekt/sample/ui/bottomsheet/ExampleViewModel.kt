package app.futured.arkitekt.sample.ui.bottomsheet

import com.thefuntasty.mvvm.rxusecases.BaseRxViewModel
import javax.inject.Inject

class ExampleViewModel @Inject constructor(
    override val viewState: ExampleViewState
) : BaseRxViewModel<ExampleViewState>() {

    fun onClose() = sendEvent(CloseEvent)
}
