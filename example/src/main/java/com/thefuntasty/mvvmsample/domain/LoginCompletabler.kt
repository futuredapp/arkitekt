package com.thefuntasty.mvvmsample.domain

import com.thefuntasty.interactors.interactors.BaseCompletabler
import com.thefuntasty.mvvmsample.data.model.User
import com.thefuntasty.mvvmsample.data.store.UserStore
import io.reactivex.Completable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LoginCompletabler @Inject constructor(
    private val userStore: UserStore
) : BaseCompletabler<LoginCompletabler.LoginData>() {

    data class LoginData(val firstName: String, val lastName: String)

    override fun prepare(args: LoginData): Completable {
        return Completable.timer(1, TimeUnit.SECONDS).andThen(
            Completable.fromCallable { userStore.setUser(User(args.firstName, args.lastName)) }
        )
    }
}
