package com.thefuntasty.mvvm.binding

import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.ViewState

class BindingActivityDelegate<VS : ViewState, B : ViewDataBinding> {

    fun initViewBinding(
        fragmentActivity: FragmentActivity,
        viewModel: BaseViewModel<VS>,
        @LayoutRes layoutResId: Int,
        bindingVariables: BindingVariables
    ): B {
        return setupBindingView(fragmentActivity, layoutResId) {
            it.setVariable(bindingVariables.viewId, this)
            it.setVariable(bindingVariables.viewModelId, viewModel)
            it.setVariable(bindingVariables.viewStateId, viewModel.viewState)
            it.lifecycleOwner = fragmentActivity
        }
    }

    private fun setupBindingView(fragmentActivity: FragmentActivity, layoutResId: Int, set: (B) -> Unit): B {
        return DataBindingUtil.setContentView<B>(fragmentActivity, layoutResId).also {
            set(it)
        }
    }
}
