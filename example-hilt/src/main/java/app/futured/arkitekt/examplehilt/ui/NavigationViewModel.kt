package app.futured.arkitekt.examplehilt.ui

import androidx.lifecycle.ViewModel
import app.futured.arkitekt.core.livedata.UiData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor() : ViewModel() {

    val graphVariable = UiData(0)
}
