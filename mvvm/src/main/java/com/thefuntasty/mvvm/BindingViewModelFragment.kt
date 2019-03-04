package com.thefuntasty.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.thefuntasty.mvvm.binding.DataBindingVariables
import com.thefuntasty.mvvm.binding.FragmentDataBindingInitializer

abstract class BindingViewModelFragment<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding>
    : ViewModelFragment<VM, VS>() {

    private val bindingFragmentDelegate = FragmentDataBindingInitializer<VS, B>()

    abstract val brViewVariableId: Int
    abstract val brViewModelVariableId: Int
    abstract val brViewStateVariableId: Int

    lateinit var binding: B

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return bindingFragmentDelegate.initDataBinding(
            this,
            viewModel,
            inflater,
            container,
            layoutResId,
            getBindingVariables()
        ).let {
            binding = it.first
            return@let it.second
        }
    }

    private fun getBindingVariables(): DataBindingVariables {
        return DataBindingVariables(brViewVariableId, brViewModelVariableId, brViewStateVariableId)
    }
}
