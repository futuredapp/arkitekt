package app.futured.arkitekt.sample.ui.main

import app.futured.arkitekt.dagger.factory.BaseViewModelFactory
import javax.inject.Inject
import javax.inject.Provider

class MainViewModelFactory @Inject constructor(
    override val viewModelProvider: Provider<MainViewModel>
) : BaseViewModelFactory<MainViewModel>() {
    override val viewModelClass = MainViewModel::class
}
