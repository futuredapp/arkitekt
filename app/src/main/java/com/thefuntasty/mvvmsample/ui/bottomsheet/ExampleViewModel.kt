package com.thefuntasty.mvvmsample.ui.bottomsheet

import com.thefuntasty.interactors.BaseRxViewModel
import javax.inject.Inject

class ExampleViewModel @Inject constructor(
    override val viewState: ExampleViewState
) : BaseRxViewModel<ExampleViewState>() {

    fun onClose() = sendEvent(CloseEvent)
}
