package com.thefuntasty.mvvmsample.ui.base

import androidx.databinding.ViewDataBinding
import com.thefuntasty.mvvm.BR
import com.thefuntasty.mvvm.BaseBindingViewModelActivity
import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.ViewState

abstract class BaseActivity<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding> :
    BaseBindingViewModelActivity<VM, VS, B>() {

    override fun getBRViewVariableId() = BR.view

    override fun getBRViewModelVariableId() = BR.viewModel

    override fun getBRViewStateVariableId() = BR.viewState
}
