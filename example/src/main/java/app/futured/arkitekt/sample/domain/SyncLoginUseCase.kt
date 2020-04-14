package app.futured.arkitekt.sample.domain

import app.futured.arkitekt.rxusecases.usecases.CompletableUseCase
import app.futured.arkitekt.sample.data.model.User
import app.futured.arkitekt.sample.data.store.UserStore
import io.reactivex.Completable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SyncLoginUseCase @Inject constructor(
    private val userStore: UserStore
) : CompletableUseCase<SyncLoginUseCase.LoginData>() {

    data class LoginData(val firstName: String, val lastName: String)

    override fun prepare(args: LoginData): Completable {
        return Completable.timer(1, TimeUnit.SECONDS).andThen(
            Completable.fromCallable { userStore.setUser(User(args.firstName, args.lastName)) }
        )
    }
}
