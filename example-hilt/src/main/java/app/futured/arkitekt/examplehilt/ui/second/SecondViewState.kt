package app.futured.arkitekt.examplehilt.ui.second

import androidx.lifecycle.SavedStateHandle
import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.examplehilt.tools.getOrThrow
import javax.inject.Inject

class SecondViewState @Inject constructor(handle: SavedStateHandle) : ViewState {
    private val handleNumber = handle.getOrThrow<Int>("number")
    val displayText = "Received by handle: $handleNumber"
}
