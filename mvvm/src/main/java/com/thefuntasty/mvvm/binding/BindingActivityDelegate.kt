package com.thefuntasty.mvvm.binding

import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.ViewState

interface BindingActivityDelegate<VS : ViewState, B : ViewDataBinding> {

    val brViewVariableId: Int
    val brViewModelVariableId: Int
    val brViewStateVariableId: Int

    fun initViewBinding(
        fragmentActivity: FragmentActivity,
        viewModel: BaseViewModel<VS>,
        @LayoutRes layoutResId: Int
    ): B {
        return setupBindingView(fragmentActivity, layoutResId) {
            it.setVariable(brViewVariableId, this)
            it.setVariable(brViewModelVariableId, viewModel)
            it.setVariable(brViewStateVariableId, viewModel.viewState)
            it.lifecycleOwner = fragmentActivity
        }
    }

    private fun setupBindingView(fragmentActivity: FragmentActivity, layoutResId: Int, set: (B) -> Unit): B {
        return DataBindingUtil.setContentView<B>(fragmentActivity, layoutResId).also {
            set(it)
        }
    }
}
