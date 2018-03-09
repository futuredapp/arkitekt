package com.thefuntasty.mvvm.factory

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.thefuntasty.mvvm.BaseViewModel
import javax.inject.Provider

abstract class BaseViewModelFactory<T : BaseViewModel> : ViewModelProvider.Factory {

    abstract val viewModel: Provider<T>

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModel.get() as T
    }
}
