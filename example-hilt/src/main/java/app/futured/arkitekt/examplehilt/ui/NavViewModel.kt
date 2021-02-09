package app.futured.arkitekt.examplehilt.ui

import androidx.lifecycle.SavedStateHandle
import app.futured.arkitekt.crusecases.BaseCrViewModel
import app.futured.arkitekt.examplehilt.ui.first.FirstViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavViewModel @Inject constructor(
    override val viewState: FirstViewState,
    val savedStateHandle: SavedStateHandle,
) : BaseCrViewModel<FirstViewState>() {

    override fun onStart() {

    }
}
