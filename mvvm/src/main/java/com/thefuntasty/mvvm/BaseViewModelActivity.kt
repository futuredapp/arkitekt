package com.thefuntasty.mvvm

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatActivity
import kotlin.reflect.KClass

abstract class BaseViewModelActivity<VM : BaseViewModel, B : ViewDataBinding> :
        AppCompatActivity(), DataBindingView<VM, B>, BaseView {

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = createViewModel()
        lifecycle.addObserver(viewModel)

        if (setupDataBinding(layoutInflater)) {
            setContentView(binding.root)
        }
    }

    override fun getViewModelFromProvider(factory: ViewModelProvider.Factory, viewModelKClass: KClass<VM>): VM {
        return ViewModelProviders.of(this, factory).get(viewModelKClass.java)
    }
}
