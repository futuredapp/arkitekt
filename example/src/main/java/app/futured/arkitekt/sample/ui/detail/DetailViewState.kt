package app.futured.arkitekt.sample.ui.detail

import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.core.livedata.DefaultValueLiveData
import javax.inject.Inject

class DetailViewState @Inject constructor() : ViewState {

    val stringNumber = DefaultValueLiveData("")

    val number = DefaultValueLiveData(0)
}
