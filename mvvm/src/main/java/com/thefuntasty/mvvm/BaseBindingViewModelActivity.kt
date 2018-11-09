package com.thefuntasty.mvvm

import androidx.databinding.ViewDataBinding

abstract class BaseBindingViewModelActivity<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding> :
    BaseViewModelActivity<VM, VS>(), BindingViewModelView<VM, B> {

    override lateinit var binding: B
}
