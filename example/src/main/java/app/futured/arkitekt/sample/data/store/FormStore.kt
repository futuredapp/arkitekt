package app.futured.arkitekt.sample.data.store

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FormStore @Inject constructor() {
    val formChannel = ConflatedBroadcastChannel<Pair<String, String>>()

    fun saveForm(form: Pair<String, String>) {
        formChannel.trySend(form)
    }

    fun getFormFlow() = formChannel.asFlow()
}
