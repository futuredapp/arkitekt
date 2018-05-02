package com.thefuntasty.mvvm

import android.databinding.ViewDataBinding

abstract class BaseBindingViewModelActivity<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding> :
        BaseViewModelActivity<VM, VS>(), BindingViewModelView<VM, B>, BaseView {

    override lateinit var binding: B
}
