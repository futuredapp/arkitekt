package app.futured.arkitekt.sample.ui.home

import app.futured.arkitekt.core.BaseCoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    override val viewState: HomeViewState
) : BaseCoreViewModel<HomeViewState>() {

    fun onDetail() {
        sendEvent(ShowDetailEvent)
    }

    fun onForm() {
        sendEvent(ShowFormEvent)
    }

    fun onLogin() {
        sendEvent(ShowLoginEvent)
    }

    fun onBottomSheet() {
        sendEvent(ShowBottomSheetEvent)
    }

    fun onLoad() {
        sendEvent(ShowLoadEvent)
    }
}
