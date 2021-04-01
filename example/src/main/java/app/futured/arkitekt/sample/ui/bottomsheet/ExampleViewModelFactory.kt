package app.futured.arkitekt.sample.ui.bottomsheet

import app.futured.arkitekt.dagger.factory.BaseViewModelFactory
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

class ExampleViewModelFactory @Inject constructor(
    override val viewModelProvider: Provider<ExampleViewModel>
) : BaseViewModelFactory<ExampleViewModel>() {
    override val viewModelClass: KClass<ExampleViewModel> = ExampleViewModel::class
}
