package com.thefuntasty.mvvm

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity

abstract class BindingViewModelActivity<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding>
    : ViewModelActivity<VM, VS>() {

    abstract val brViewVariableId: Int
    abstract val brViewModelVariableId: Int
    abstract val brViewStateVariableId: Int

    lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = setupBindingView(this, layoutResId) {
            it.setVariable(brViewVariableId, this)
            it.setVariable(brViewModelVariableId, viewModel)
            it.setVariable(brViewStateVariableId, viewModel.viewState)
            it.lifecycleOwner = this
        }
    }

    private fun setupBindingView(fragmentActivity: FragmentActivity, layoutResId: Int, set: (B) -> Unit): B {
        return DataBindingUtil.setContentView<B>(fragmentActivity, layoutResId).also {
            set(it)
        }
    }
}
