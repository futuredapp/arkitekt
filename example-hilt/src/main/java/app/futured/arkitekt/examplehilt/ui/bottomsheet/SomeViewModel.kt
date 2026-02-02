package app.futured.arkitekt.examplehilt.ui.bottomsheet

import app.futured.arkitekt.crusecases.BaseLegacyViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SomeViewModel @Inject constructor(
    override val viewState: SomeViewState
) : BaseLegacyViewModel<SomeViewState>() {

    fun onClose() = sendEvent(CloseEvent)
}
