package com.thefuntasty.mvvm

import com.thefuntasty.mvvm.factory.BaseViewModelFactory

interface ViewModelView<VM : BaseViewModel<*>> {

    var viewModel: VM

    val viewModelFactory: BaseViewModelFactory<VM>

    fun createViewModel(): VM
}
