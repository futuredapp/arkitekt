package com.thefuntasty.mvvm

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.thefuntasty.mvvm.delegate.BindingActivityDelegate

abstract class BindingViewModelActivity<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding>
    : ViewModelActivity<VM, VS>(), BindingActivityDelegate<VS, B> {

    override lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = initViewBinding(this, viewModel, layoutResId)
    }
}
