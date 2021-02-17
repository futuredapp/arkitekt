package app.futured.arkitekt.dagger

import app.futured.arkitekt.core.BaseViewModel
import app.futured.arkitekt.dagger.factory.ViewModelFactory

/**
 * Holds reference to BaseViewModelFactory associated with particular ViewModel.
 * Forces developer to specify ViewModelFactory in its specific Activity/Fragment.
 */
interface ViewModelCreator<VM : BaseViewModel<*>> {

    val viewModelFactory: ViewModelFactory<VM>
}
