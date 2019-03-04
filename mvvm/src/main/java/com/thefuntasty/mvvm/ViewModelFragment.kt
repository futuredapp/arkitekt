package com.thefuntasty.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.thefuntasty.mvvm.viewmodel.ViewModelFragmentDelegate

abstract class ViewModelFragment<VM : BaseViewModel<VS>, VS : ViewState> : Fragment(),
    ViewModelFragmentDelegate<VM, VS> {

    override lateinit var viewModel: VM

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = initViewModel(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(layoutResId, container, false)

    private inline fun <reified IVM : BaseViewModel<VS>> getVM(): IVM =
        ViewModelProviders.of(this, viewModelFactory).get(IVM::class.java)

    @Suppress("UNCHECKED_CAST")
    override fun getViewModelFromProvider(): VM = getVM<BaseViewModel<VS>>() as VM

    inline fun <VS : ViewState, reified VM : BaseViewModel<VS>> getActivityViewModel(): VM =
        ViewModelProviders.of(requireActivity()).get(VM::class.java)
}
