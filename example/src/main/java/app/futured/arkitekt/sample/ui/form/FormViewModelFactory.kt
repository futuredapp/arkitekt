package app.futured.arkitekt.sample.ui.form

import app.futured.arkitekt.dagger.factory.BaseViewModelFactory
import javax.inject.Inject
import javax.inject.Provider

class FormViewModelFactory @Inject constructor(
    override val viewModelProvider: Provider<FormViewModel>
) : BaseViewModelFactory<FormViewModel>() {
    override val viewModelClass = FormViewModel::class
}
