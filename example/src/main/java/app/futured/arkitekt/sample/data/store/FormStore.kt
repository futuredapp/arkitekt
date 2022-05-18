package app.futured.arkitekt.sample.data.store

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FormStore @Inject constructor() {
    private val formChannel = MutableStateFlow<Pair<String, String>?>(null)

    fun saveForm(form: Pair<String, String>) {
        formChannel.value = form
    }

    fun getFormFlow() = formChannel.filterNotNull()
}
