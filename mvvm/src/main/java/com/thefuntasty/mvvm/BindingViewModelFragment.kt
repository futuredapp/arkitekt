package com.thefuntasty.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.thefuntasty.mvvm.delegate.BindingFragmentDelegate

abstract class BindingViewModelFragment<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding>
    : ViewModelFragment<VM, VS>(), BindingFragmentDelegate<VS, B> {

    override lateinit var binding: B

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return initViewBinding(this, viewModel, inflater, container, layoutResId).let {
            binding = it.first
            return@let it.second
        }
    }
}
