package com.thefuntasty.mvvm.delegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.ViewState

interface BindingFragmentDelegate<VS : ViewState, B : ViewDataBinding> {

    val brViewVariableId: Int
    val brViewModelVariableId: Int
    val brViewStateVariableId: Int
    var binding: B

    fun initViewBinding(
        fragment: Fragment,
        viewModel: BaseViewModel<VS>,
        inflater: LayoutInflater,
        container: ViewGroup?,
        layoutResId: Int
    ): Pair<B, View> {
        return setupBindingView(inflater, container, layoutResId) {
            it.setVariable(brViewVariableId, this)
            it.setVariable(brViewModelVariableId, viewModel)
            it.setVariable(brViewStateVariableId, viewModel.viewState)
            it.setLifecycleOwner(fragment)
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
