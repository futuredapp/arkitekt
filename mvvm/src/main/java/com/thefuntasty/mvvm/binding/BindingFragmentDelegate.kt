package com.thefuntasty.mvvm.binding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.ViewState

class BindingFragmentDelegate<VS : ViewState, B : ViewDataBinding> {

    fun initViewBinding(
        fragment: Fragment,
        viewModel: BaseViewModel<VS>,
        inflater: LayoutInflater,
        container: ViewGroup?,
        @LayoutRes layoutResId: Int,
        bindingVariables: BindingVariables
    ): Pair<B, View> {
        return setupBindingView(inflater, container, layoutResId) {
            it.setVariable(bindingVariables.viewId, this)
            it.setVariable(bindingVariables.viewModelId, viewModel)
            it.setVariable(bindingVariables.viewStateId, viewModel.viewState)
            it.lifecycleOwner = fragment.viewLifecycleOwner
        }
    }

    private fun setupBindingView(
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        layoutResId: Int,
        set: (B) -> Unit
    ): Pair<B, View> {
        val binding = DataBindingUtil.inflate<B>(layoutInflater, layoutResId, container, false).also {
            set(it)
        }
        return Pair(binding, binding.root)
    }
}
