package app.futured.arkitekt.dagger.factory

import androidx.lifecycle.ViewModelProvider
import app.futured.arkitekt.core.BaseLegacyCoreViewModel
import kotlin.reflect.KClass

interface ViewModelFactory<T : BaseLegacyCoreViewModel<*>> : ViewModelProvider.Factory {

    val viewModelClass: KClass<T>
}
