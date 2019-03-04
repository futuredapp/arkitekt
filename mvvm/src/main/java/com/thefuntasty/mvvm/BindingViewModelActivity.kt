package com.thefuntasty.mvvm

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.thefuntasty.mvvm.binding.BindingActivityDelegate
import com.thefuntasty.mvvm.binding.BindingVariables

abstract class BindingViewModelActivity<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding>
    : ViewModelActivity<VM, VS>() {

    private val bindingActivityDelegate = BindingActivityDelegate<VS, B>()

    abstract val brViewVariableId: Int
    abstract val brViewModelVariableId: Int
    abstract val brViewStateVariableId: Int


    lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingActivityDelegate.initViewBinding(this, viewModel, layoutResId, getBindingVariables())
    }

    private fun getBindingVariables(): BindingVariables {
        return BindingVariables(brViewVariableId, brViewModelVariableId, brViewStateVariableId)
    }
}
