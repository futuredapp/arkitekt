package app.futured.arkitekt.sample.domain

import app.futured.arkitekt.crusecases.UseCase
import app.futured.arkitekt.sample.data.model.User
import app.futured.arkitekt.sample.data.store.UserStore
import kotlinx.coroutines.delay
import javax.inject.Inject

class SyncLoginUseCase @Inject constructor(
    private val userStore: UserStore
) : UseCase<SyncLoginUseCase.LoginData, Unit>() {

    data class LoginData(val firstName: String, val lastName: String)

    override suspend fun build(args: LoginData) {
        delay(1_000)
        userStore.setUser(User(args.firstName, args.lastName))
    }
}
