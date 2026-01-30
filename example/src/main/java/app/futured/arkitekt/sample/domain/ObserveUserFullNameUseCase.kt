package app.futured.arkitekt.sample.domain

import app.futured.arkitekt.crusecases.FlowUseCase
import app.futured.arkitekt.sample.data.store.UserStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveUserFullNameUseCase @Inject constructor(
    private val userStore: UserStore
) : FlowUseCase<Unit, String>() {

    override fun build(args: Unit): Flow<String> =
        userStore.getUser().map { "${it.firstName} ${it.lastName}" }
}
