package app.futured.arkitekt.examplehilt.ui.second

import app.futured.arkitekt.crusecases.BaseCrViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SecondViewModel @Inject constructor(
    override val viewState: SecondViewState
) : BaseCrViewModel<SecondViewState>()


