package com.thefuntasty.mvvm.crinteractors.testinteractors.testinteractors

import com.thefuntasty.mvvm.crinteractors.BaseFlowInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TestFlowFailureInteractor : BaseFlowInteractor<Unit>() {

    private lateinit var error: Throwable

    fun init(errorToThrow: Throwable) = apply {
        this.error = errorToThrow
    }

    override fun build(): Flow<Unit> = flow {
        throw error
    }
}
