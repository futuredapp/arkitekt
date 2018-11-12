package com.thefuntasty.mvvm

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseBindingViewModelActivity<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding> :
    BaseViewModelActivity<VM, VS>() {

    private lateinit var binding: B

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBindingView {
            it.setVariable(getBRViewVariableId(), this)
            it.setVariable(getBRViewModelVariableId(), viewModel)
            it.setVariable(getBRViewStateVariableId(), viewModel.viewState)
            it.setLifecycleOwner(this)
        }
    }

    private fun setupBindingView(set: (B) -> Unit): View? {
        return DataBindingUtil.setContentView<B>(this, getLayoutResId()).let {
            binding = it
            set(binding)
            binding.root
        }
    }

    abstract fun getBRViewVariableId(): Int

    abstract fun getBRViewModelVariableId(): Int

    abstract fun getBRViewStateVariableId(): Int
}
