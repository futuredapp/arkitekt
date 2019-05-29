package com.thefuntasty.mvvmsample.domain

import com.thefuntasty.interactors.BaseCompletabler
import com.thefuntasty.mvvmsample.data.model.User
import com.thefuntasty.mvvmsample.data.store.UserStore
import io.reactivex.Completable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LoginCompletabler @Inject constructor(
    private val userStore: UserStore
) : BaseCompletabler() {

    private lateinit var firstName: String
    private lateinit var lastName: String

    fun init(firstName: String, lastName: String) = apply {
        this.firstName = firstName
        this.lastName = lastName
    }

    override fun prepare(): Completable {
        return Completable.timer(1, TimeUnit.SECONDS).andThen(
            Completable.fromCallable { userStore.setUser(User(firstName, lastName)) }
        )
    }
}
