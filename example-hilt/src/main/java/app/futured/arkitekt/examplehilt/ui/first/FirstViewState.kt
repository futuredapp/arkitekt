package app.futured.arkitekt.examplehilt.ui.first

import android.content.res.Resources
import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.core.livedata.UiData
import app.futured.arkitekt.sample.hilt.R
import javax.inject.Inject

class FirstViewState @Inject constructor(resources: Resources) : ViewState {
    val randomNumber = UiData(0)
    val displayText = randomNumber.map { resources.getString(R.string.random_number, it) }
}
