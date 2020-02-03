package com.thefuntasty.mvvmsample.ui.coroutinesresult

import com.thefuntasty.mvvm.factory.BaseViewModelFactory
import javax.inject.Inject
import javax.inject.Provider

class CoroutinesResultViewModelFactory @Inject constructor(
    override val viewModelProvider: Provider<CoroutinesResultViewModel>
) : BaseViewModelFactory<CoroutinesResultViewModel>() {
    override val viewModelClass = CoroutinesResultViewModel::class
}
