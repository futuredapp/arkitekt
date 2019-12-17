package com.thefuntasty.mvvm.crinteractors.testinteractors.testinteractors

import com.thefuntasty.mvvm.crinteractors.BaseFlowInteractor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.onEach
import kotlin.properties.Delegates

class TestFlowInteractor : BaseFlowInteractor<Int>() {

    private lateinit var listToEmit: List<Int>
    private var delayBetweenEmits: Long by Delegates.notNull()

    fun init(listToEmit: List<Int>, delayBetweenEmits: Long) = apply {
        this.listToEmit = listToEmit
        this.delayBetweenEmits = delayBetweenEmits
    }

    override fun build(): Flow<Int> = listToEmit.asFlow().onEach { delay(delayBetweenEmits) }
}
