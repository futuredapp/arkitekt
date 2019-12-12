package com.thefuntasty.mvvm.crusecases.testusecases

import com.thefuntasty.mvvm.crusecases.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TestFailureFlowUseCase : FlowUseCase<Throwable, Unit>() {

    override fun build(args: Throwable): Flow<Unit> = flow {
        throw args
    }
}
