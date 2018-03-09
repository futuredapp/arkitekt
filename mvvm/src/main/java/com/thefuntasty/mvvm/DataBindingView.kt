package com.thefuntasty.mvvm

import android.arch.lifecycle.ViewModelProvider
import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import kotlin.reflect.KClass

internal interface DataBindingView<VM : BaseViewModel, B : ViewDataBinding> {

    var viewModel: VM
    var binding: B

    fun createViewModel(): VM

    fun inflateBindingLayout(layoutInflater: LayoutInflater): B?

    fun getViewModelFromProvider(factory: ViewModelProvider.Factory, viewModelKClass: KClass<VM>): VM

    fun setupDataBinding(layoutInflater: LayoutInflater): Boolean {
        inflateBindingLayout(layoutInflater)?.let {
            binding = it
            binding.setVariable(BR.view, this)
            binding.setVariable(BR.viewModel, viewModel)
            return true
        }
        return false
    }
}
