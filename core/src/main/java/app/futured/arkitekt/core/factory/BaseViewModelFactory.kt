package app.futured.arkitekt.core.factory

import androidx.lifecycle.ViewModel
import app.futured.arkitekt.core.BaseViewModel
import javax.inject.Provider
import kotlin.reflect.KClass

/**
 * Base class for creating instances of ViewModel. Instance of this factory is required
 * to be available in Activity/Fragment for automatic ViewModel instance creation.
 * Factory might be automatically generated and should look like this:
 *
 *  class FormViewModelFactory @Inject constructor(
 *     override val viewModelProvider: Provider<FormViewModel>
 *  ) : BaseViewModelFactory<FormViewModel>() {
 *     override val viewModelClass = FormViewModel::class
 *  }
 */
abstract class BaseViewModelFactory<T : BaseViewModel<*>>: ViewModelFactory<T> {

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

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return viewModelProvider.get() as T
    }
}
