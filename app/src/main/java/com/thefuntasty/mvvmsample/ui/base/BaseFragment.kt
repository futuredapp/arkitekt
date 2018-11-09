package com.thefuntasty.mvvmsample.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.thefuntasty.mvvm.BR
import com.thefuntasty.mvvm.BaseBindingViewModelFragment
import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.ViewState

abstract class BaseFragment<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding> :
    BaseBindingViewModelFragment<VM, VS, B>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setViewBinding(layoutInflater) {
            it.setVariable(BR.view, this)
            it.setVariable(BR.viewModel, viewModel)
            it.setVariable(BR.viewState, viewModel.viewState)
            it.setLifecycleOwner(this)
        }?.let { return it }
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}
