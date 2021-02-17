package app.futured.arkitekt.examplehilt.ui

import app.futured.arkitekt.crusecases.BaseCrViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    override val viewState: MainViewState
) : BaseCrViewModel<MainViewState>()
