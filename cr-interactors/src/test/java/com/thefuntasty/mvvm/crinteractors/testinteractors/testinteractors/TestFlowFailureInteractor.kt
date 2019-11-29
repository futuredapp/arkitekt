package com.thefuntasty.mvvm.crinteractors.testinteractors.testinteractors

import com.thefuntasty.mvvm.crinteractors.BaseFlower
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TestFlowFailureInteractor : BaseFlower<Throwable, Unit>() {

    override suspend fun build(args: Throwable): Flow<Unit> = flow {
        throw args
    }
}
