package com.thefuntasty.mvvmsample.data.store

import com.jakewharton.rxrelay2.BehaviorRelay
import com.thefuntasty.mvvmsample.data.model.User
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserStore @Inject constructor() {
    private val userRelay = BehaviorRelay.createDefault(User.EMPTY)

    fun setUser(user: User) {
        userRelay.accept(user)
        // ... optionally persist user
    }

    fun getUser(): Observable<User> = userRelay.hide()
}
