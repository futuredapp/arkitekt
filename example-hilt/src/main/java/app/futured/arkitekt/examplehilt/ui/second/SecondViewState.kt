package app.futured.arkitekt.examplehilt.ui.second

import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.examplehilt.tools.getOrThrow
import app.futured.arkitekt.sample.hilt.R
import javax.inject.Inject

class SecondViewState @Inject constructor(
    handle: SavedStateHandle,
    resources: Resources
) : ViewState {
    private val randomNumber = handle.getOrThrow<Int>("number")
    val displayText = resources.getString(R.string.received_by_handle, randomNumber)
}
