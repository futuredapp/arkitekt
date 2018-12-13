package com.thefuntasty.mvvm

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.thefuntasty.mvvm.delegate.ViewModelActivityDelegate

abstract class ViewModelActivity<VM : BaseViewModel<VS>, VS : ViewState>
    : AppCompatActivity(), ViewModelActivityDelegate<VM, VS> {

    override lateinit var viewModel: VM

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = initViewModel(this)
    }

    private inline fun <reified IVM : BaseViewModel<VS>> getVM(): IVM =
        ViewModelProviders.of(this, viewModelFactory).get(IVM::class.java)

    @Suppress("UNCHECKED_CAST")
    override fun getViewModelFromProvider(): VM = getVM<BaseViewModel<VS>>() as VM
}
