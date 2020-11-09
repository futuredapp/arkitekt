package app.futured.arkitekt.sample.ui.main

import androidx.savedstate.SavedStateRegistryOwner
import app.futured.arkitekt.core.factory.BaseSavedStateViewModelFactory
import javax.inject.Inject
import javax.inject.Provider

class MainViewModelFactory @Inject constructor(
    savedStateRegistryOwner: SavedStateRegistryOwner,
    override val viewModelProvider: Provider<MainViewModel>
) : BaseSavedStateViewModelFactory<MainViewModel>(savedStateRegistryOwner) {
    override val viewModelClass = MainViewModel::class
}
