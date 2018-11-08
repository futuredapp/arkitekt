package com.thefuntasty.mvvmsample.ui.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.thefuntasty.mvvm.BR
import com.thefuntasty.mvvm.BaseBindingViewModelActivity
import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.ViewState

abstract class BaseActivity<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding> :
        BaseBindingViewModelActivity<VM, VS, B>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView(layoutInflater) {
            it.setVariable(BR.view, this)
            it.setVariable(BR.viewModel, viewModel)
            it.setVariable(BR.viewState, viewModel.viewState)
            it.setLifecycleOwner(this)
        }.let {
            setContentView(it)
        }
    }
}
