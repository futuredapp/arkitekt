package com.thefuntasty.mvvm

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModelProviders
import com.thefuntasty.mvvm.event.Event
import dagger.android.support.DaggerFragment
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
abstract class BaseViewModelFragment<VM : BaseViewModel<VS>, VS : ViewState> :
    DaggerFragment(), ViewModelView<VM>, BaseView {

    lateinit var viewModel: VM

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel().apply { lifecycle.addObserver(this) }
    }

    protected inline fun <reified VM : BaseViewModel<VS>> getViewModelFromProvider(): VM =
        ViewModelProviders.of(this, viewModelFactory).get(VM::class.java)

    protected fun <EVENT : Event<VS>> observeEvent(event: KClass<out EVENT>, observer: (EVENT) -> Unit) {
        viewModel.observeEvent(this, event, observer as (Event<VS>) -> Unit)
    }

    protected inline fun <VS : ViewState, reified VM : BaseViewModel<VS>> getActivityViewModel(): VM =
        ViewModelProviders.of(requireActivity()).get(VM::class.java)

    override fun createViewModel(): VM = getViewModelFromProvider<BaseViewModel<VS>>() as VM

    @LayoutRes
    abstract fun getLayoutResId() : Int
}
