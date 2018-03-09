package com.thefuntasty.mvvm

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.CallSuper
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import kotlin.reflect.KClass

abstract class BaseViewModelActivity<VM : BaseViewModel, B : ViewDataBinding> :
        DaggerAppCompatActivity(), DataBindingView<VM, B>, BaseView {

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidInjection.inject(this)

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
