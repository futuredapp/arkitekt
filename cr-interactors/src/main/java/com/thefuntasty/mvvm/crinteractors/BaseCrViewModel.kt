package com.thefuntasty.mvvm.crinteractors

import androidx.lifecycle.viewModelScope
import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.ViewState
import kotlinx.coroutines.CoroutineScope

abstract class BaseCrViewModel<S : ViewState> : BaseViewModel<S>(), CoroutineScopeOwner {

    override val coroutineScope: CoroutineScope = viewModelScope
}
