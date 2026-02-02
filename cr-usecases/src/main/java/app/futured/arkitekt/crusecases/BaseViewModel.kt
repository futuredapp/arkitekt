package app.futured.arkitekt.crusecases

import androidx.lifecycle.viewModelScope
import app.futured.arkitekt.core.BaseCoreViewModel
import app.futured.arkitekt.core.ViewState
import kotlinx.coroutines.CoroutineScope

abstract class BaseViewModel<S : ViewState>() : BaseCoreViewModel<S>(), CoroutineScopeOwner {
    override val coroutineScope: CoroutineScope = viewModelScope
}