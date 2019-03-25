package com.thefuntasty.mvvm.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thefuntasty.mvvm.BaseViewModel
import javax.inject.Provider
import kotlin.reflect.KClass

abstract class BaseViewModelFactory<T : BaseViewModel<*>> : ViewModelProvider.Factory {

    abstract val viewModelProvider: Provider<T>
    abstract val viewModelClass: KClass<T>

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return viewModelProvider.get() as T
    }
}
