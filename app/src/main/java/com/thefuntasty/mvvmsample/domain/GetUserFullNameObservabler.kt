package com.thefuntasty.mvvmsample.domain

import com.thefuntasty.interactors.BaseObservabler
import com.thefuntasty.mvvmsample.data.store.UserStore
import io.reactivex.Observable
import javax.inject.Inject

class GetUserFullNameObservabler @Inject constructor(
    private val userStore: UserStore
) : BaseObservabler<String>() {

    override fun prepare(): Observable<String> {
        return userStore.getUser()
            .map { "${it.firstName} ${it.lastName}" }
    }
}
