package com.thefuntasty.mvvm

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseViewModelFragment<VM : BaseViewModel, B : ViewDataBinding> :
        Fragment(), DataBindingView<VM, B>, BaseView {

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = createViewModel()
        lifecycle.addObserver(viewModel)

        setupDataBinding(layoutInflater)
        return binding.root
    }
}
