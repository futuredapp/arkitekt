package com.thefuntasty.mvvm.crusecases.testinteractors.testinteractors

import com.thefuntasty.mvvm.crusecases.BaseFlowUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.onEach

class TestFlowUseCase : BaseFlowUseCase<TestFlowUseCase.Data, Int>() {

    data class Data(
        val listToEmit: List<Int>,
        val delayBetweenEmits: Long
    )

    override suspend fun build(args: Data): Flow<Int> =
        args.listToEmit.asFlow().onEach { delay(args.delayBetweenEmits) }
}
