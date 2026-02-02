package app.futured.arkitekt.sample.ui.main

import app.futured.arkitekt.core.BaseCoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    override val viewState: MainViewState
) : BaseCoreViewModel<MainViewState>() {

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
