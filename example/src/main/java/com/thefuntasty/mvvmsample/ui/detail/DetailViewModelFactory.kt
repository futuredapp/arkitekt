package com.thefuntasty.mvvmsample.ui.detail

import app.futured.arkitekt.core.factory.BaseViewModelFactory
import javax.inject.Inject
import javax.inject.Provider

class DetailViewModelFactory @Inject constructor(
    override val viewModelProvider: Provider<DetailViewModel>
) : BaseViewModelFactory<DetailViewModel>() {
    override val viewModelClass = DetailViewModel::class
}
