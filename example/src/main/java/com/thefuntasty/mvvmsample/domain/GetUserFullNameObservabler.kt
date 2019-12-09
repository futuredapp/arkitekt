package com.thefuntasty.mvvmsample.domain

import com.thefuntasty.mvvm.rxusecases.usecases.ObservablerUseCase
import com.thefuntasty.mvvmsample.data.store.UserStore
import io.reactivex.Observable
import javax.inject.Inject

class GetUserFullNameObservabler @Inject constructor(
    private val userStore: UserStore
) : ObservablerUseCase<Unit, String>() {

    override fun prepare(args: Unit): Observable<String> =
        userStore.getUser().map { "${it.firstName} ${it.lastName}" }
}
