package com.thefuntasty.mvvmsample.ui.detail

import com.thefuntasty.mvvm.factory.BaseViewModelFactory
import javax.inject.Inject
import javax.inject.Provider

class DetailViewModelFactory @Inject constructor(
        override val viewModelProvider: Provider<DetailViewModel>
) : BaseViewModelFactory<DetailViewModel>()
