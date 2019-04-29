package com.thefuntasty.mvvmsample.ui.bottomsheet

import com.thefuntasty.mvvm.factory.BaseViewModelFactory
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

class ExampleViewModelFactory @Inject constructor(
    override val viewModelProvider: Provider<ExampleViewModel>
) : BaseViewModelFactory<ExampleViewModel>() {
    override val viewModelClass: KClass<ExampleViewModel> = ExampleViewModel::class
}
