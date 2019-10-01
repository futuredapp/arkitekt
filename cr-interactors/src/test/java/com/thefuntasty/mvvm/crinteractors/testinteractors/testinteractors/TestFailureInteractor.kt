package com.thefuntasty.mvvm.crinteractors.testinteractors.testinteractors

import com.thefuntasty.mvvm.crinteractors.BaseCoroutineInteractor

class TestFailureInteractor : BaseCoroutineInteractor<Unit>() {

    private lateinit var error: Throwable

    fun init(errorToThrow: Throwable) = apply {
        this.error = errorToThrow
    }

    override suspend fun build() {
        throw error
    }
}
