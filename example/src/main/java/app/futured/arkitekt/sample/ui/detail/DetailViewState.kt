package app.futured.arkitekt.sample.ui.detail

import androidx.lifecycle.MutableLiveData
import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.core.livedata.DefaultValueLiveData
import javax.inject.Inject

class DetailViewState @Inject constructor() : ViewState {

    val stringNumber = MutableLiveData("")

    val number = DefaultValueLiveData(0)
}
