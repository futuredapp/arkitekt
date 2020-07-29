package app.futured.arkitekt.examplehilt.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import app.futured.arkitekt.core.BaseViewModel
import app.futured.arkitekt.examplehilt.domain.SampleUseCase

class MainViewModel @ViewModelInject constructor(
    /*private val sampleUseCase: SampleUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle,*/
    override val viewState: MainViewState
) : BaseViewModel<MainViewState>() {

}
