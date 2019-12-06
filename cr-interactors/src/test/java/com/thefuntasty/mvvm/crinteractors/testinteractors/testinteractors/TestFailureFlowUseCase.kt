package com.thefuntasty.mvvm.crinteractors.testinteractors.testinteractors

import com.thefuntasty.mvvm.crinteractors.BaseFlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TestFailureFlowUseCase : BaseFlowUseCase<Throwable, Unit>() {

    override suspend fun build(args: Throwable): Flow<Unit> = flow {
        throw args
    }
}
