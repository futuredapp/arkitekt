package com.thefuntasty.mvvm.rxusecases

import androidx.lifecycle.LiveData
import app.futured.arkitekt.core.BaseViewModel
import app.futured.arkitekt.core.ViewState
import com.thefuntasty.mvvm.rxusecases.disposables.DisposablesOwner
import com.thefuntasty.mvvm.rxusecases.usecases.ObservableUseCase
import io.reactivex.disposables.CompositeDisposable

/**
 * Base ViewModel class prepared for providing data to UI through [LiveData] and
 * obtaining data from Stores (Repositories) by executing RxJava based use cases
 * eg. [ObservableUseCase].
 */
abstract class BaseRxViewModel<S : ViewState> : BaseViewModel<S>(), DisposablesOwner {

    /**
     * CompositeDisposable of all recently executed use cases which is cleared when
     * ViewModel is no longer used and will be destroyed.
     */
    override val disposables = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
