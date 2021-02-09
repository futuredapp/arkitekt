package app.futured.arkitekt.examplehilt.ui

import androidx.hilt.lifecycle.ViewModelInject
import app.futured.arkitekt.crusecases.BaseCrViewModel

//@HiltViewModel // TODO: not working annotation in hilt jetpack 1.0.0-alpha03
class MainViewModel @ViewModelInject constructor(
    override val viewState: MainViewState
) : BaseCrViewModel<MainViewState>() {

}
