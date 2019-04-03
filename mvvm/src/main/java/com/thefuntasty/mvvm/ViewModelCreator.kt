package com.thefuntasty.mvvm

import com.thefuntasty.mvvm.factory.BaseViewModelFactory

/**
 * Holds reference to BaseViewModelFactory associated with particular ViewModel.
 * Forces developer to specify ViewModelFactory in its specific Activity/Fragment.
 */
interface ViewModelCreator<VM : BaseViewModel<*>> {

    val viewModelFactory: BaseViewModelFactory<VM>
}
