package com.thefuntasty.mvvmsample.ui.main

import app.futured.arkitekt.core.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(override val viewState: MainViewState) : BaseViewModel<MainViewState>()
