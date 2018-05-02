package com.thefuntasty.mvvm

import android.arch.lifecycle.ViewModelProviders
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.CallSuper
import com.thefuntasty.mvvm.event.Event
import dagger.android.support.DaggerFragment
import kotlin.reflect.KClass

abstract class BaseBindingViewModelFragment<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding> :
        DaggerFragment(), BindingViewModelView<VM, B>, BaseView {

    override lateinit var binding: B

    override lateinit var viewModel: VM

    protected inline fun <reified VM : BaseViewModel<VS>> getViewModelFromProvider(): VM {
        return ViewModelProviders.of(this, viewModelFactory).get(VM::class.java)
    }

    protected inline fun <reified VM : BaseViewModel<VS>> getActivityViewModel(): VM {
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
