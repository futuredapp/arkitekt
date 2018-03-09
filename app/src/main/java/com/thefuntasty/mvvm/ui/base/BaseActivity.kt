package com.thefuntasty.mvvm.ui.base

import android.databinding.ViewDataBinding
import android.os.Bundle
import com.thefuntasty.mvvm.BR
import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.BaseViewModelActivity
import dagger.android.AndroidInjection

abstract class BaseActivity<VM : BaseViewModel, B : ViewDataBinding> : BaseViewModelActivity<VM, B>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        viewModel = createViewModel()
        lifecycle.addObserver(viewModel)

        inflateBindingLayout(layoutInflater)?.let {
            binding = it
            binding.setVariable(BR.view, this)
            binding.setVariable(BR.viewModel, viewModel)
            setContentView(binding.root)
        }
    }
}
