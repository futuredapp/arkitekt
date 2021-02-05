package app.futured.arkitekt.examplehilt.ui

import androidx.lifecycle.SavedStateHandle
import app.futured.arkitekt.core.BaseViewModel
import app.futured.arkitekt.examplehilt.domain.SampleUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class MainViewModel @AssistedInject constructor(
    private val sampleUseCase: SampleUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle,
    override val viewState: MainViewState
) : BaseViewModel<MainViewState>() {

}
