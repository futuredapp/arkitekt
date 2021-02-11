package app.futured.arkitekt.examplehilt.ui.first

import androidx.lifecycle.map
import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.core.livedata.DefaultValueLiveData
import javax.inject.Inject

class FirstViewState @Inject constructor() : ViewState {
    val randomNumber = DefaultValueLiveData(0)
    val displayText = randomNumber.map { "Random number: $it" }
}
