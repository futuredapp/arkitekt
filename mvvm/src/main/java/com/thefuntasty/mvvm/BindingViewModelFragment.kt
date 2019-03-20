package com.thefuntasty.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BindingViewModelFragment<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding>
    : ViewModelFragment<VM, VS>() {

    abstract val brViewVariableId: Int
    abstract val brViewModelVariableId: Int
    abstract val brViewStateVariableId: Int

    lateinit var binding: B

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return setupBindingView(inflater, container, layoutResId) {
            it.setVariable(brViewVariableId, this)
            it.setVariable(brViewModelVariableId, viewModel)
            it.setVariable(brViewStateVariableId, viewModel.viewState)
            it.lifecycleOwner = this.viewLifecycleOwner
            binding = it
        }
    }

    private fun setupBindingView(
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        layoutResId: Int,
        set: (B) -> Unit
    ): View {
        val binding = DataBindingUtil.inflate<B>(layoutInflater, layoutResId, container, false).also {
            set(it)
        }
        return binding.root
    }
}
