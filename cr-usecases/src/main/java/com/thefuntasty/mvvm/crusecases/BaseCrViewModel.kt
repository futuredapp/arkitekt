package com.thefuntasty.mvvm.crusecases

import androidx.lifecycle.viewModelScope
import app.futured.arkitekt.core.BaseViewModel
import app.futured.arkitekt.core.ViewState
import kotlinx.coroutines.CoroutineScope

/**
 * Base ViewModel class prepared for providing data to UI through [LiveData] and
 * obtaining data from Stores (Repositories) by executing Coroutine based use cases like
 * [UseCase] and [FlowUseCase].
 */
abstract class BaseCrViewModel<S : ViewState> : BaseViewModel<S>(), CoroutineScopeOwner {

    override val coroutineScope: CoroutineScope = viewModelScope
}
