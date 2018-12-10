package com.thefuntasty.mvvmsample.domain

import com.thefuntasty.interactors.BaseSingler
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

typealias User = Pair<String, String>

class LoginInteractor @Inject constructor() : BaseSingler<User>() {

    private lateinit var name: String
    private lateinit var surname: String

    companion object {
        private const val DELAY_MS = 2000L
    }

    fun init(name: String, surname: String) = apply {
        this.name = name
        this.surname = surname
    }

    override fun prepare(): Single<User> {
        return Single.fromCallable {
            name to surname
        }.delay(DELAY_MS, TimeUnit.MILLISECONDS)
    }
}
