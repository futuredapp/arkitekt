package app.futured.arkitekt.examplehilt.ui.bottomsheet

import androidx.lifecycle.SavedStateHandle
import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.examplehilt.tools.getOrThrow
import javax.inject.Inject

class SomeViewState @Inject constructor(handle: SavedStateHandle) : ViewState {
    val number = handle.getOrThrow<Int>("number")
}
