package app.futured.arkitekt.sample.ui.main

import app.futured.arkitekt.core.BaseLegacyCoreViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(override val viewState: MainViewState) : BaseLegacyCoreViewModel<MainViewState>()
