package com.thefuntasty.mvvmsample.ui.form

import com.thefuntasty.mvvm.factory.BaseViewModelFactory
import javax.inject.Inject
import javax.inject.Provider

class FormViewModelFactory @Inject constructor(
    override val viewModelProvider: Provider<FormViewModel>
) : BaseViewModelFactory<FormViewModel>() {
    override val viewModelClass = FormViewModel::class
}
