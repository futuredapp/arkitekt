package com.thefuntasty.mvvm.crinteractors.testinteractors.testinteractors

import com.thefuntasty.mvvm.crinteractors.BaseFlowUsecase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.onEach

class TestFlowUsecase : BaseFlowUsecase<TestFlowUsecase.Data, Int>() {

    data class Data(
        val listToEmit: List<Int>,
        val delayBetweenEmits: Long
    )

    override suspend fun build(args: Data): Flow<Int> =
        args.listToEmit.asFlow().onEach { delay(args.delayBetweenEmits) }
}
