package com.thefuntasty.mvvm

import com.thefuntasty.mvvm.factory.BaseViewModelFactory

interface ViewModelView<VM : BaseViewModel<*>> {

    val layoutResId: Int

    val viewModel: VM

    val viewModelFactory: BaseViewModelFactory<VM>

    fun getViewModelFromProvider(): VM

    fun createViewModel(): VM = getViewModelFromProvider()
}
