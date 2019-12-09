package com.thefuntasty.mvvm.crusecases.testinteractors.testinteractors

import com.thefuntasty.mvvm.crusecases.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TestFailureFlowUseCase : FlowUseCase<Throwable, Unit>() {

    override suspend fun build(args: Throwable): Flow<Unit> = flow {
        throw args
    }
}
