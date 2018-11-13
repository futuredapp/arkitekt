package com.thefuntasty.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseBindingViewModelFragment<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding> :
    BaseViewModelFragment<VM, VS>() {

    abstract val brViewVariableId: Int
    abstract val brViewModelVariableId: Int
    abstract val brViewStateVariableId: Int

    lateinit var binding: B

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return setupBindingView(layoutInflater, container) {
            it.setVariable(brViewVariableId, this)
            it.setVariable(brViewModelVariableId, viewModel)
            it.setVariable(brViewStateVariableId, viewModel.viewState)
            it.setLifecycleOwner(viewLifecycleOwner)
        }
    }

    private fun setupBindingView(layoutInflater: LayoutInflater, container: ViewGroup?, set: (B) -> Unit): View? {
        return DataBindingUtil.inflate<B>(layoutInflater, layoutResId, container, false).let {
            binding = it
            set(binding)
            binding.root
        }
    }
}
