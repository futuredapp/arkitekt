package com.thefuntasty.mvvm

import com.thefuntasty.mvvm.factory.BaseViewModelFactory

interface ViewModelCreator<VM : BaseViewModel<*>> {

    val viewModelFactory: BaseViewModelFactory<VM>
}
