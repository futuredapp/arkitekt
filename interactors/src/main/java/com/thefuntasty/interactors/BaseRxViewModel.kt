package com.thefuntasty.interactors

import com.thefuntasty.interactors.disposables.DisposablesOwner
import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.ViewState
import io.reactivex.disposables.CompositeDisposable

abstract class BaseRxViewModel<S : ViewState> : BaseViewModel<S>(), DisposablesOwner {

    override val disposables = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
