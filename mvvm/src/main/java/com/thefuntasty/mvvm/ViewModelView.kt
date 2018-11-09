package com.thefuntasty.mvvm

import com.thefuntasty.mvvm.factory.BaseViewModelFactory

interface ViewModelView<VM : BaseViewModel<*>> {

    val viewModelFactory: BaseViewModelFactory<VM>

    fun createViewModel(): VM
}
