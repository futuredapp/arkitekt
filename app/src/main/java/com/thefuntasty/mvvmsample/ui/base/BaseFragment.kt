package com.thefuntasty.mvvmsample.ui.base

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thefuntasty.mvvm.BR
import com.thefuntasty.mvvm.BaseBindingViewModelFragment
import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.ViewState

abstract class BaseFragment<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding> :
        BaseBindingViewModelFragment<VM, VS, B>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setView(layoutInflater) {
            it.setVariable(BR.view, this)
            it.setVariable(BR.viewModel, viewModel)
            it.setVariable(BR.viewState, viewModel.viewState)
            it.setLifecycleOwner(this)
        }?.let { return it }
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}
