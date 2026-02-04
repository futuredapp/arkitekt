package app.futured.arkitekt.sample.ui.detail

import androidx.compose.runtime.mutableStateOf
import app.futured.arkitekt.core.ViewState
import javax.inject.Inject

class DetailViewState @Inject constructor() : ViewState {

    val stringNumber = mutableStateOf("")

    val number = mutableStateOf(0)
}
