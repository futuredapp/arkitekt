package com.thefuntasty.mvvm

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment

abstract class BaseViewModelFragment<VM : BaseViewModel, B : ViewDataBinding> :
        DaggerFragment(), DataBindingView<VM, B>, BaseView {

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        AndroidSupportInjection.inject(this)

        viewModel = createViewModel()
        lifecycle.addObserver(viewModel)

        setupDataBinding(layoutInflater)
        return binding.root
    }
}
