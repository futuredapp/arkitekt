package app.futured.arkitekt.dagger.factory

import androidx.lifecycle.ViewModelProvider
import app.futured.arkitekt.core.BaseViewModel
import kotlin.reflect.KClass

interface ViewModelFactory<T : BaseViewModel<*>>: ViewModelProvider.Factory {

    val viewModelClass: KClass<T>
}
