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

    lateinit var binding: B

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return setupBindingView(layoutInflater, container) {
            it.setVariable(getBRViewVariableId(), this)
            it.setVariable(getBRViewModelVariableId(), viewModel)
            it.setVariable(getBRViewStateVariableId(), viewModel.viewState)
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

    abstract fun getBRViewVariableId(): Int

    abstract fun getBRViewModelVariableId(): Int

    abstract fun getBRViewStateVariableId(): Int
}
