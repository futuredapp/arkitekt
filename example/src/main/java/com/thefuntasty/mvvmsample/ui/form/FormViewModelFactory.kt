package com.thefuntasty.mvvmsample.ui.form

import app.futured.arkitekt.core.factory.BaseViewModelFactory
import javax.inject.Inject
import javax.inject.Provider

class FormViewModelFactory @Inject constructor(
    override val viewModelProvider: Provider<FormViewModel>
) : BaseViewModelFactory<FormViewModel>() {
    override val viewModelClass = FormViewModel::class
}
