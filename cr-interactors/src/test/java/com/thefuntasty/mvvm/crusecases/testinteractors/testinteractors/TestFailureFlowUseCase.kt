package com.thefuntasty.mvvm.crusecases.testinteractors.testinteractors

import com.thefuntasty.mvvm.crusecases.BaseFlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TestFailureFlowUseCase : BaseFlowUseCase<Throwable, Unit>() {

    override suspend fun build(args: Throwable): Flow<Unit> = flow {
        throw args
    }
}
