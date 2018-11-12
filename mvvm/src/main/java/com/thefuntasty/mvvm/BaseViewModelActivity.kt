package com.thefuntasty.mvvm

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModelProviders
import com.thefuntasty.mvvm.event.Event
import dagger.android.support.DaggerAppCompatActivity
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
abstract class BaseViewModelActivity<VM : BaseViewModel<VS>, VS : ViewState> :
    DaggerAppCompatActivity(), ViewModelView<VM>, BaseView {

    lateinit var viewModel: VM

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel().apply { lifecycle.addObserver(this) }
    }

    private inline fun <reified VM : BaseViewModel<VS>> getViewModelFromProvider(): VM =
        ViewModelProviders.of(this, viewModelFactory).get(VM::class.java)

    protected fun <E : Event<VS>> observeEvent(event: KClass<out E>, observer: (E) -> Unit) {
        viewModel.observeEvent(this, event, observer as (Event<VS>) -> Unit)
    }

    override fun createViewModel(): VM = getViewModelFromProvider<BaseViewModel<VS>>() as VM

    @LayoutRes
    abstract fun getLayoutResId() : Int
}
