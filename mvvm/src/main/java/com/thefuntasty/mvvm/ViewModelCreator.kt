package com.thefuntasty.mvvm

import com.thefuntasty.mvvm.factory.BaseViewModelFactory
import kotlin.reflect.KClass

interface ViewModelCreator<VM : BaseViewModel<*>> {

    val viewModelFactory: BaseViewModelFactory<VM>

    val viewModelClass: KClass<VM>
}
