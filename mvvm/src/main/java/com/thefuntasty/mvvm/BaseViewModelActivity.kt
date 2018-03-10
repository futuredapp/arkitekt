package com.thefuntasty.mvvm

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.CallSuper
import com.thefuntasty.mvvm.event.Event
import com.thefuntasty.mvvm.factory.BaseViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import kotlin.reflect.KClass

abstract class BaseViewModelActivity<VM : BaseViewModel> : DaggerAppCompatActivity() {

    protected lateinit var viewModel: VM

    abstract fun createViewModel(): VM

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = createViewModel()
        lifecycle.addObserver(viewModel)
    }

    protected fun getViewModelFromProvider(factory: BaseViewModelFactory<VM>, viewModelKClass: KClass<VM>): VM {
        return ViewModelProviders.of(this, factory).get(viewModelKClass.java)
    }

    protected fun <T : Event> observerEvent(event: KClass<T>, observer: (T) -> Unit) {
        viewModel.observeEvent(this, event, observer)
    }
}
