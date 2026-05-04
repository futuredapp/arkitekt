package app.futured.arkitekt.compose

import androidx.lifecycle.viewModelScope
import app.futured.arkitekt.core.BaseCoreViewModel
import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.crusecases.CoroutineScopeOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

abstract class BaseViewModel<S : ViewState> :
    BaseCoreViewModel<S>(),
    CoroutineScopeOwner {
    override val useCaseScope: CoroutineScope = viewModelScope
    override val useCaseJobPool: MutableMap<Any, Job> = mutableMapOf()
}
