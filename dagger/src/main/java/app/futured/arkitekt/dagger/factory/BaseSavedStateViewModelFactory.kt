package app.futured.arkitekt.dagger.factory

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import app.futured.arkitekt.core.BaseViewModel
import javax.inject.Provider
import kotlin.reflect.KClass

abstract class BaseSavedStateViewModelFactory <T : BaseViewModel<*>>(
    savedStateRegistryOwner: SavedStateRegistryOwner
) : AbstractSavedStateViewModelFactory(savedStateRegistryOwner, null), ViewModelFactory<T> {

    /**
     * ViewModel provider definition. Provider<VM> is automatically
     * generated when ViewModel constructor has @Inject annotation.
     */
    abstract val viewModelProvider: Provider<T>
    /**
     * ViewModel class definition eg:
     *
     *     override val viewModelClass = FormViewModel::class
     */
    abstract override val viewModelClass: KClass<T>

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return viewModelProvider.get().apply {
            this.internalSavedStateHandle = handle
        } as? T ?: throw IllegalStateException("Unknown ViewModel class")
    }
}
