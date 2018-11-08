package com.thefuntasty.mvvm

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModelProviders
import com.thefuntasty.mvvm.event.Event
import dagger.android.support.DaggerFragment
import kotlin.reflect.KClass

abstract class BaseViewModelFragment<VM : BaseViewModel<VS>, VS : ViewState> :
        DaggerFragment(), ViewModelView<VM>, BaseView {

    override lateinit var viewModel: VM

    protected inline fun <reified VM : BaseViewModel<VS>> getViewModelFromProvider(): VM {
        return ViewModelProviders.of(this, viewModelFactory).get(VM::class.java)
    }

    protected inline fun <VS : ViewState, reified VM : BaseViewModel<VS>> getActivityViewModel(): VM {
        return ViewModelProviders.of(requireActivity()).get(VM::class.java)
    }

    protected fun <EVENT : Event<VS>> observeEvent(event: KClass<out EVENT>, observer: (EVENT) -> Unit) {
        @Suppress("UNCHECKED_CAST")
        viewModel.observeEvent(this, event, observer as (Event<VS>) -> Unit)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel().apply { lifecycle.addObserver(this) }
    }
}
