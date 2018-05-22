package com.thefuntasty.mvvm

import android.databinding.ViewDataBinding

abstract class BaseBindingViewModelFragment<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding> :
        BaseViewModelFragment<VM, VS>(), BindingViewModelView<VM, B> {

    override lateinit var binding: B
}
