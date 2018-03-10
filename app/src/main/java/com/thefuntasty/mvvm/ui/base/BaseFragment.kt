package com.thefuntasty.mvvm.ui.base

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thefuntasty.mvvm.BR
import com.thefuntasty.mvvm.BaseBindingViewModelFragment
import com.thefuntasty.mvvm.BaseViewModel
import dagger.android.support.AndroidSupportInjection

abstract class BaseFragment<VM : BaseViewModel, B : ViewDataBinding> : BaseBindingViewModelFragment<VM, B>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        AndroidSupportInjection.inject(this)
        viewModel = createViewModel()
        lifecycle.addObserver(viewModel)

        inflateBindingLayout(layoutInflater)?.let {
            binding = it
            binding.setVariable(BR.view, this)
            binding.setVariable(BR.viewModel, viewModel)
            return binding.root
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}
