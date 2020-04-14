package app.futured.arkitekt.sample.domain

import app.futured.arkitekt.crusecases.FlowUseCase
import app.futured.arkitekt.sample.data.store.FormStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFormUseCase @Inject constructor(
    private val formStore: FormStore
) : FlowUseCase<Unit, Pair<String, String>>() {
    override fun build(args: Unit): Flow<Pair<String, String>> = formStore.getFormFlow()
}
