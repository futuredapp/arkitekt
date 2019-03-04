package com.thefuntasty.mvvm

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.thefuntasty.mvvm.binding.ActivityDataBindingInitializer
import com.thefuntasty.mvvm.binding.DataBindingVariables

abstract class BindingViewModelActivity<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding>
    : ViewModelActivity<VM, VS>() {

    private val bindingActivityDelegate = ActivityDataBindingInitializer<VS, B>()

    abstract val brViewVariableId: Int
    abstract val brViewModelVariableId: Int
    abstract val brViewStateVariableId: Int

    lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingActivityDelegate.initDataBinding(this, viewModel, layoutResId, getBindingVariables())
    }

    private fun getBindingVariables(): DataBindingVariables {
        return DataBindingVariables(brViewVariableId, brViewModelVariableId, brViewStateVariableId)
    }
}
