package com.thefuntasty.mvvm

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseBindingViewModelActivity<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding> :
    BaseViewModelActivity<VM, VS>() {

    abstract val brViewVariableId: Int
    abstract val brViewModelVariableId: Int
    abstract val brViewStateVariableId: Int

    protected lateinit var binding: B

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBindingView {
            it.setVariable(brViewVariableId, this)
            it.setVariable(brViewModelVariableId, viewModel)
            it.setVariable(brViewStateVariableId, viewModel.viewState)
            it.setLifecycleOwner(this)
        }
    }

    private fun setupBindingView(set: (B) -> Unit): View? {
        return DataBindingUtil.setContentView<B>(this, layoutResId).let {
            binding = it
            set(binding)
            binding.root
        }
    }
}
