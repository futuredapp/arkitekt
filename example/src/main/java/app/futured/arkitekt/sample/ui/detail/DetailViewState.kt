package app.futured.arkitekt.sample.ui.detail

import androidx.lifecycle.MutableLiveData
import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.core.livedata.DefaultValueLiveData
import app.futured.arkitekt.core.livedata.UiData
import javax.inject.Inject

class DetailViewState @Inject constructor() : ViewState {

    val stringNumber = MutableLiveData("")

    val number = UiData(0)
}
