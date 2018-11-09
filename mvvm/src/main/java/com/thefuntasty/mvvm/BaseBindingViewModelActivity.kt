package com.thefuntasty.mvvm

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding

abstract class BaseBindingViewModelActivity<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding> :
    BaseViewModelActivity<VM, VS>(), BindingViewModelView<VM, B> {

    override lateinit var binding: B

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val boundedView = setupBindingView(layoutInflater) {
            it.setVariable(getBRViewVariableId(), this)
            it.setVariable(getBRViewModelVariableId(), viewModel)
            it.setVariable(getBRViewStateVariableId(), viewModel.viewState)
            it.setLifecycleOwner(this)
        }
        setContentView(boundedView)
    }

    abstract fun getBRViewVariableId(): Int

    abstract fun getBRViewModelVariableId(): Int

    abstract fun getBRViewStateVariableId(): Int
}
