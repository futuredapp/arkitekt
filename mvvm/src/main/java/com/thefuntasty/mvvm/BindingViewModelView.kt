package com.thefuntasty.mvvm

import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.View

interface BindingViewModelView<VM : BaseViewModel<*>, B : ViewDataBinding> : ViewModelView<VM>, BaseView {

    var binding: B

    fun inflateBindingLayout(layoutInflater: LayoutInflater): B?

    fun setView(layoutInflater: LayoutInflater, set: (B) -> Unit): View? {
        return inflateBindingLayout(layoutInflater)?.let {
            binding = it
            set(binding)
            binding.root
        }
    }
}
