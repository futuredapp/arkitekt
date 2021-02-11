package app.futured.arkitekt.examplehilt.ui

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import app.futured.arkitekt.core.livedata.UiData
import app.futured.arkitekt.sample.hilt.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(resources: Resources) : ViewModel() {

    val graphVariable = UiData("")

    val outputMessage = graphVariable.map {
        resources.getString(R.string.user_name, it)
    }
}
