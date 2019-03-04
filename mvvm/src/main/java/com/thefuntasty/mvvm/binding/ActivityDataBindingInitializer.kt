package com.thefuntasty.mvvm.binding

import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.ViewState

class ActivityDataBindingInitializer<VS : ViewState, B : ViewDataBinding> {

    fun initDataBinding(
        fragmentActivity: FragmentActivity,
        viewModel: BaseViewModel<VS>,
        @LayoutRes layoutResId: Int,
        dataBindingVariables: DataBindingVariables
    ): B {
        return setupBindingView(fragmentActivity, layoutResId) {
            it.setVariable(dataBindingVariables.viewId, this)
            it.setVariable(dataBindingVariables.viewModelId, viewModel)
            it.setVariable(dataBindingVariables.viewStateId, viewModel.viewState)
            it.lifecycleOwner = fragmentActivity
        }
    }

    private fun setupBindingView(fragmentActivity: FragmentActivity, layoutResId: Int, set: (B) -> Unit): B {
        return DataBindingUtil.setContentView<B>(fragmentActivity, layoutResId).also {
            set(it)
        }
    }
}
