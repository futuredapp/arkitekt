package app.futured.arkitekt.sample.ui.detail

import app.futured.arkitekt.compose.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    override val viewState: DetailViewState
) : BaseViewModel<DetailViewState>() {

    fun incrementNumber() {
        viewState.number.value = viewState.number.value + 1
    }

    fun onBack() {
        sendEvent(NavigateBackEvent)
    }
}
